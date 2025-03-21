package org.project.by.booking.controller;

import lombok.RequiredArgsConstructor;
import org.project.by.booking.service.BookingService;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.project.by.common.constants.dto.event.RideEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/history/passenger/{passengerId}")
    public PageDto<RidesDto> getPassengerHistory(@PathVariable("passengerId") Long passengerId, Pageable pageable) {
        return bookingService.getPassengerHistory(passengerId, pageable);
    }

    @GetMapping("/history/driver/{driverId}")
    public PageDto<RidesDto> getDriverHistory(@PathVariable("driverId") Long driverId, Pageable pageable) {
        return bookingService.getDriverHistory(driverId, pageable);
    }

    @PostMapping("/ride/cancel")
    public void cancelRide(@RequestBody RideEvent rideCancelledEvent) {
        bookingService.cancelRide(rideCancelledEvent);
    }

}
