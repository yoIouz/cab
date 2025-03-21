package org.project.by.passenger.service;

import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.project.by.common.constants.dto.event.BookingRequestEvent;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.passenger.dto.PassengerDto;
import org.springframework.data.domain.Pageable;

public interface PassengerService {

    PassengerDto getPassenger(Long id);

    void editPassenger(PassengerDto passengerDTO);

    void rateDriver(UserRatingEvent ratingDTO);

    void saveRating(UserRatingEvent ratingDto);

    void requestBooking(BookingRequestEvent bookingRequestEvent);

    PageDto<RidesDto> getRideHistory(Long passengerId, Pageable pageable);

    void acknowledgeCompleted(RideEvent rideCompleted);

    void acknowledgeCancelled(RideEvent rideCancelled);

}
