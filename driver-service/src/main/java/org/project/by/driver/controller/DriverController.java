package org.project.by.driver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.driver.dto.DriverDto;
import org.project.by.driver.service.DriverService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/driver")
public class DriverController {

    private final DriverService driverService;

    @GetMapping("/{id}")
    public DriverDto getDriverProfile(@PathVariable("id") Long driverId) {
        return driverService.getDriverProfile(driverId);
    }

    @PatchMapping("/edit")
    public void editDriverProfile(@RequestBody DriverDto driverDto) {
        driverService.editProfile(driverDto);
    }

    @PostMapping("/rate")
    public void ratePassenger(@RequestBody @Valid UserRatingEvent ratingDto) {
        driverService.ratePassenger(ratingDto);
    }

    @GetMapping("/history/{id}")
    public PageDto<RidesDto> findRideHistory(@PathVariable("id") Long driverId,
                                             Pageable pageable) {
        return driverService.getRideHistory(driverId, pageable);
    }

    @PostMapping("/ride/accept")
    public void acceptRide(@RequestBody @Valid RideEvent rideAccepted) {
        driverService.acceptRide(rideAccepted);
    }

    @PostMapping("/ride/complete")
    public void completeRide(@RequestBody @Valid RideEvent rideCompleted) {
        driverService.completeRide(rideCompleted);
    }

    @PostMapping("/ride/cancel")
    public void cancelRide(@RequestBody @Valid RideEvent rideCancelled) {
        driverService.cancelRide(rideCancelled);
    }

}

