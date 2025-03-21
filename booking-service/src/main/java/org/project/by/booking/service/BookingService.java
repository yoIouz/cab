package org.project.by.booking.service;

import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.project.by.common.constants.dto.event.BookingRequestEvent;
import org.project.by.common.constants.dto.event.CompletedRideEvent;
import org.project.by.common.constants.dto.event.RideEvent;
import org.springframework.data.domain.Pageable;

public interface BookingService {

    void processBookingRequest(BookingRequestEvent bookingRequestEvent);

    void updateRideStatus(BookingRequestEvent ride);

    void completeRide(CompletedRideEvent rideCompleted);

    PageDto<RidesDto> getPassengerHistory(Long passengerId, Pageable pageable);

    PageDto<RidesDto> getDriverHistory(Long driver, Pageable pageable);

    void cancelRide(RideEvent rideCancelledEvent);

}
