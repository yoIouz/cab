package org.project.by.passenger.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.project.by.common.constants.dto.event.BookingRequestEvent;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.passenger.dto.PassengerDto;
import org.project.by.passenger.keycloak.KeycloakClient;
import org.project.by.passenger.keycloak.KeycloakResponse;
import org.project.by.passenger.service.PassengerService;
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
@RequestMapping("/api/passenger")
public class PassengerController {

    private final KeycloakClient keycloakClient;

    private final PassengerService passengerService;

    @PostMapping("/refresh-token")
    public KeycloakResponse refreshToken(String refreshToken) {
        return keycloakClient.refreshToken(refreshToken);
    }

    @GetMapping("/{id}")
    public PassengerDto getProfile(@PathVariable("id") Long id) {
        return passengerService.getPassenger(id);
    }

    @PatchMapping("/edit")
    public void editProfile(@RequestBody PassengerDto passengerDto) {
        passengerService.editPassenger(passengerDto);
    }

    @PostMapping("/rate")
    public void rateDriver(@RequestBody @Valid UserRatingEvent ratingDto) {
        passengerService.rateDriver(ratingDto);
    }

    @PostMapping("/create")
    public void createBooking(@RequestBody @Valid BookingRequestEvent bookingRequestEvent) {
        passengerService.requestBooking(bookingRequestEvent);
    }

    @GetMapping("/history/{id}")
    public PageDto<RidesDto> findRideHistory(@PathVariable("id") Long passengerId, Pageable pageable) {
        return passengerService.getRideHistory(passengerId, pageable);
    }

}
