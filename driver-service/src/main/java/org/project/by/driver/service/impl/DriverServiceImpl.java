package org.project.by.driver.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.project.by.common.constants.dto.event.AcceptedRideEvent;
import org.project.by.common.constants.dto.event.CompletedRideEvent;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.driver.dto.DriverDto;
import org.project.by.driver.entity.Driver;
import org.project.by.driver.entity.DriverStatus;
import org.project.by.driver.feign.BookingClient;
import org.project.by.driver.kafka.producer.DriverProducer;
import org.project.by.driver.mapper.DriverMapper;
import org.project.by.driver.repository.DriverRepository;
import org.project.by.driver.service.DriverService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.project.by.common.constants.kafka.KafkaConstants.ACCEPTED_RIDES_TOPIC;
import static org.project.by.common.constants.kafka.KafkaConstants.COMPLETED_RIDES_TOPIC;
import static org.project.by.common.constants.kafka.KafkaConstants.PASSENGER_RATING_PARTITION;
import static org.project.by.common.constants.kafka.KafkaConstants.RATING_TOPIC;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverMapper driverMapper;

    private final DriverRepository driverRepository;

    private final DriverProducer driverProducer;

    private final BookingClient bookingClient;

    @Override
    public DriverDto getDriverProfile(Long id) {
        return driverMapper.toDtoDriver(this.findDriverById(id));
    }

    @Override
    public void editProfile(DriverDto driverDto) {
        Driver driver = this.findDriverById(driverDto.driverId());
        driverMapper.updateDriverFromDto(driverDto, driver);
        driverRepository.save(driver);
    }

    @Override
    public void ratePassenger(UserRatingEvent ratingDto) {
        driverProducer.send(ratingDto, RATING_TOPIC, PASSENGER_RATING_PARTITION);
    }

    @Override
    @Transactional
    public void saveRating(UserRatingEvent ratingDto) {
        driverRepository.updateRating(ratingDto.id(), ratingDto.rating());
    }

    @Override
    @Transactional("transactionManager")
    public void acceptRide(AcceptedRideEvent rideAccepted) {
        Driver driver = this.findDriverById(rideAccepted.getDriverId());
        DriverStatus driverStatus = driver.getIsBusy();
        if (Objects.nonNull(driverStatus) && !driverStatus.getIsBusy()) {
            driverStatus.setIsBusy(true);
            driverRepository.save(driver);
            driverProducer.send(rideAccepted, ACCEPTED_RIDES_TOPIC, null);
        }
    }

    @Override
    @Cacheable(value = "history", key = "#driverId", unless = "#result.isEmpty()")
    @Retryable(retryFor = {TimeoutException.class, SocketTimeoutException.class, FeignException.class},
            backoff = @Backoff(delay = 3000, multiplier = 2))
    public PageDto<RidesDto> getRideHistory(Long driverId, Pageable pageable) {
        return bookingClient.getDriverHistory(driverId, pageable);
    }

    @Override
    @CachePut(value = "history", key = "#rideCompleted.driverId")
    @Transactional("transactionManager")
    public void completeRide(CompletedRideEvent rideCompleted) {
        Driver driver = this.findDriverById(rideCompleted.getDriverId());
        DriverStatus driverStatus = driver.getIsBusy();
        if (Objects.nonNull(driverStatus)) {
            driverStatus.setIsBusy(false);
            driverRepository.save(driver);
            driverProducer.send(rideCompleted, COMPLETED_RIDES_TOPIC, null);
        }
    }

    @Override
    @CacheEvict(value = "history", key = "#rideCancelled.driverId")
    @Retryable(retryFor = {TimeoutException.class, SocketTimeoutException.class, FeignException.class},
            backoff = @Backoff(delay = 3000, multiplier = 2))
    public void cancelRide(RideEvent rideCancelled) {
        bookingClient.cancelRide(rideCancelled);
    }

    private Driver findDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
    }

}
