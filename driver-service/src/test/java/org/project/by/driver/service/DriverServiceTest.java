package org.project.by.driver.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.driver.SucceededWatcher;
import org.project.by.driver.dto.DriverDto;
import org.project.by.driver.entity.Driver;
import org.project.by.driver.entity.DriverStatus;
import org.project.by.driver.kafka.producer.DriverProducer;
import org.project.by.driver.mapper.DriverMapperImpl;
import org.project.by.driver.repository.DriverRepository;
import org.project.by.driver.service.impl.DriverServiceImpl;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.project.by.common.constants.kafka.KafkaConstants.ACCEPTED_RIDES_TOPIC;
import static org.project.by.common.constants.kafka.KafkaConstants.COMPLETED_RIDES_TOPIC;

@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class, SucceededWatcher.class})
public class DriverServiceTest {

    @Mock
    DriverRepository driverRepository;

    @Mock
    DriverMapperImpl driverMapper;

    @Mock
    DriverProducer driverProducer;

    @InjectMocks
    DriverServiceImpl driverService;

    @Test
    void shouldGetDriverProfile() {
        Long driverId = 1L;
        Driver driver = new Driver();
        driver.setId(driverId);
        driver.setCar("3194AX-7");
        driver.setName("Ахмед");
        DriverDto driverDto = new DriverDto(driverId, "Ахмед", "3194AX-7", null);

        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driver));
        when(driverMapper.toDtoDriver(driver)).thenReturn(driverDto);
        DriverDto result = driverService.getDriverProfile(driverId);

        assertThat(result.driverId()).isEqualTo(driverDto.driverId());
        assertThat(result.name()).isEqualTo(driverDto.name());
        assertThat(result.car()).isEqualTo(driverDto.car());
        assertThat(result.rating()).isNull();

        verify(driverRepository).findById(driverId);
        verify(driverMapper).toDtoDriver(driver);
    }

    @Test
    void shouldEditProfile() {
        Long driverId = 1L;
        DriverDto driverDto = new DriverDto(driverId, "Махмуд", "3194AX-7", null);
        Driver driver = new Driver();
        driver.setId(driverId);
        driver.setCar("3194AX-7");
        driver.setName("Ахмед");

        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driver));
        driverService.editProfile(driverDto);

        verify(driverRepository).findById(driverId);
        verify(driverMapper).updateDriverFromDto(driverDto, driver);
        verify(driverRepository).save(driver);
    }

    @Test
    void shouldDoNothingIfDriverNotFound() {
        Long driverId = 1L;
        DriverDto driverDto = new DriverDto(driverId, "Махмуд", "3194AX-7", null);

        when(driverRepository.findById(driverId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> driverService.editProfile(driverDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Driver not found");

        verify(driverRepository).findById(driverId);
        verify(driverMapper, never()).updateDriverFromDto(driverDto, null);
        verify(driverRepository, never()).save(any());
    }

    @Test
    void shouldSaveRating() {
        UserRatingEvent userRatingEvent = new UserRatingEvent(1L, 5);
        driverService.saveRating(userRatingEvent);
        verify(driverRepository).updateRating(userRatingEvent.id(), userRatingEvent.rating());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldAcceptRide(Boolean isBusy) {
        Long driverId = 1L;
        RideEvent acceptedRideEvent = new RideEvent();
        acceptedRideEvent.setDate(LocalDateTime.of(2025, 3, 13, 12, 0));
        acceptedRideEvent.setPrice(new BigDecimal("30.00"));
        acceptedRideEvent.setDriverId(driverId);
        acceptedRideEvent.setRideId(1L);
        acceptedRideEvent.setPassengerId(1L);

        Driver driver = new Driver();
        driver.setId(driverId);
        driver.setCar("3194AX-7");
        driver.setName("Ахмед");
        DriverStatus driverStatus = new DriverStatus(driverId, driver, isBusy);
        driver.setIsBusy(driverStatus);
        ArgumentCaptor<RideEvent> eventCaptor = ArgumentCaptor.forClass(RideEvent.class);

        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driver));
        driverService.acceptRide(acceptedRideEvent);

        if (isBusy) {
            assertThat(driverStatus.getIsBusy()).isTrue();
            verify(driverRepository, never()).save(any());
            verify(driverProducer, never()).send(any(), any(), any());
        } else {
            assertThat(driverStatus.getIsBusy()).isTrue();
            verify(driverRepository).save(any(Driver.class));
            verify(driverProducer).send(eventCaptor.capture(), eq(ACCEPTED_RIDES_TOPIC), isNull());
            assertThat(eventCaptor.getValue()).usingRecursiveComparison().isEqualTo(acceptedRideEvent);
        }
    }

    @Test
    void shouldCompleteRide() {
        Long driverId = 1L;
        RideEvent completedRide = new RideEvent();
        completedRide.setDate(LocalDateTime.of(2025, 3, 13, 12, 0));
        completedRide.setPrice(new BigDecimal("30.00"));
        completedRide.setDriverId(driverId);
        completedRide.setRideId(1L);
        completedRide.setPassengerId(1L);

        Driver driver = new Driver();
        driver.setId(driverId);
        driver.setCar("3194AX-7");
        driver.setName("Ахмед");
        DriverStatus driverStatus = new DriverStatus(driverId, driver, Boolean.TRUE);
        driver.setIsBusy(driverStatus);
        ArgumentCaptor<RideEvent> eventCaptor = ArgumentCaptor.forClass(RideEvent.class);
        ArgumentCaptor<Driver> driverCaptor = ArgumentCaptor.forClass(Driver.class);

        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driver));
        driverService.completeRide(completedRide);

        verify(driverRepository).findById(driverId);
        verify(driverRepository).save(driverCaptor.capture());
        verify(driverProducer).send(eventCaptor.capture(), eq(COMPLETED_RIDES_TOPIC), isNull());

        assertThat(driverCaptor.getValue().getIsBusy().getIsBusy()).isFalse();
        assertThat(driverCaptor.getValue()).usingRecursiveComparison().isEqualTo(driver);
        assertThat(eventCaptor.getValue()).usingRecursiveComparison().isEqualTo(completedRide);
    }

}
