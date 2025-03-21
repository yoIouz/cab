package org.project.by.passenger.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.passenger.SucceededWatcher;
import org.project.by.passenger.dto.PassengerDto;
import org.project.by.passenger.entity.Passenger;
import org.project.by.passenger.mapper.PassengerMapper;
import org.project.by.passenger.repository.PassengerRepository;
import org.project.by.passenger.service.impl.PassengerServiceImpl;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class, SucceededWatcher.class})
class PassengerServiceTest {

    @Mock
    PassengerRepository passengerRepository;

    @Mock
    PassengerMapper passengerMapper;

    @InjectMocks
    PassengerServiceImpl passengerService;

    @Test
    void shouldReturnPassengerProfile() {
        Long passengerId = 1L;
        Passenger passenger = new Passenger(passengerId, "John", "Doe",
                "John_doe@gmail.com", 2.0F, LocalDateTime.now(), 1);
        PassengerDto passengerDto = new PassengerDto(passengerId, "John", "Doe",
                "John_doe@gmail.com");
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        when(passengerMapper.toPassengerDto(passenger)).thenReturn(passengerDto);

        PassengerDto result = passengerService.getPassenger(passengerId);
        assertThat(passenger).isNotNull();
        assertThat(passengerDto.id()).isEqualTo(result.id());
        assertThat(passengerDto.firstName()).isEqualTo(result.firstName());
        assertThat(passengerDto.lastName()).isEqualTo(result.lastName());
        assertThat(passengerDto.email()).isEqualTo(result.email());

        verify(passengerRepository, times(1)).findById(passengerId);
        verify(passengerMapper, times(1)).toPassengerDto(passenger);
    }

    @Test
    void shouldReturnPassengerProfileNotFound() {
        Long passengerId = 1L;
        Optional<Passenger> passenger = Optional.empty();
        when(passengerRepository.findById(passengerId)).thenReturn(passenger);

        PassengerDto result = passengerService.getPassenger(passengerId);
        assertThat(result).isNull();
        verify(passengerRepository, times(1)).findById(passengerId);
        verify(passengerMapper, never()).toPassengerDto(any());
    }

    @Test
    void shouldUpdatePassengerProfile() {
        PassengerDto passengerDto = new PassengerDto(1L, "Jane", "Doe",
                "Jane_doe@gmail.com");
        Passenger passenger = new Passenger(1L, "John", "Doe",
                "John_doe@gmail.com", 2.0F, LocalDateTime.now(), 1);

        when(passengerRepository.findById(passengerDto.id())).thenReturn(Optional.of(passenger));
        passengerService.editPassenger(passengerDto);

        verify(passengerMapper).updatePassengerFromDto(passengerDto, passenger);
        verify(passengerRepository).save(any(Passenger.class));
        verify(passengerRepository).findById(passengerDto.id());
    }

    @Test
    void shouldSaveRating() {
        UserRatingEvent userRatingEvent = new UserRatingEvent(1L, 5);
        passengerService.saveRating(userRatingEvent);
        verify(passengerRepository).updateRating(userRatingEvent.id(), userRatingEvent.rating());
    }

}
