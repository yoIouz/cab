package org.project.by.passenger.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.project.by.common.constants.dto.event.BookingRequestEvent;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.project.by.passenger.dto.PassengerDto;
import org.project.by.passenger.feign.BookingClient;
import org.project.by.passenger.kafka.producer.PassengerProducer;
import org.project.by.passenger.mapper.PassengerMapper;
import org.project.by.passenger.repository.PassengerRepository;
import org.project.by.passenger.service.PassengerService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    private final PassengerMapper passengerMapper;

    private final PassengerProducer passengerProducer;

    private final BookingClient bookingClient;

    @Override
    public PassengerDto getPassenger(Long id) {
        return passengerRepository.findById(id)
                .map(passengerMapper::toPassengerDto)
                .orElse(null);
    }

    @Override
    public void editPassenger(PassengerDto passengerDto) {
        passengerRepository.findById(passengerDto.id())
                .ifPresent(passenger -> {
                    passengerMapper.updatePassengerFromDto(passengerDto, passenger);
                    passengerRepository.save(passenger);
                });
    }

    @Override
    public void rateDriver(UserRatingEvent ratingDTO) {
        passengerProducer.send(ratingDTO,
                KafkaConstants.RATING_TOPIC,
                KafkaConstants.DRIVER_RATING_PARTITION);
    }

    @Override
    @Transactional
    public void saveRating(UserRatingEvent ratingDto) {
        passengerRepository.updateRating(ratingDto.id(), ratingDto.rating());
    }

    @Override
    public void requestBooking(BookingRequestEvent bookingRequestEvent) {
        passengerProducer.send(bookingRequestEvent, KafkaConstants.REQUEST_TOPIC, null);
    }

    @Override
    @Cacheable(value = "history", key = "#passengerId", unless = "#result.isEmpty()")
    @Retryable(retryFor = {TimeoutException.class, SocketTimeoutException.class, FeignException.class},
            backoff = @Backoff(delay = 3000, multiplier = 2))
    public PageDto<RidesDto> getRideHistory(Long passengerId, Pageable pageable) {
        return bookingClient.getPassengerHistory(passengerId, pageable);
    }

    @Override
    @CachePut(value = "history", key = "#rideCompleted.passengerId")
    public void acknowledgeCompleted(RideEvent rideCompleted) {
    }

    @Override
    @CacheEvict(value = "history", key = "#rideCancelled.passengerId")
    public void acknowledgeCancelled(RideEvent rideCancelled) {
    }
}
