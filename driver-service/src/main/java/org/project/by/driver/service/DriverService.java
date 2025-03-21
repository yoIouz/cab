package org.project.by.driver.service;

import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.project.by.common.constants.dto.event.AcceptedRideEvent;
import org.project.by.common.constants.dto.event.CompletedRideEvent;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.driver.dto.DriverDto;
import org.springframework.data.domain.Pageable;

public interface DriverService {

    DriverDto getDriverProfile(Long id);

    void editProfile(DriverDto driverDto);

    void ratePassenger(UserRatingEvent ratingDto);

    void saveRating(UserRatingEvent ratingDto);

    PageDto<RidesDto> getRideHistory(Long driverId, Pageable pageable);

    void completeRide(CompletedRideEvent rideCompleted);

    void cancelRide(RideEvent rideCancelled);

    void acceptRide(AcceptedRideEvent rideAccepted);

}
