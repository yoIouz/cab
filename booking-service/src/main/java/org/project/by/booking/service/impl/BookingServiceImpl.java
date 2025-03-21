package org.project.by.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.project.by.booking.entity.Ride;
import org.project.by.booking.kafka.BookingProducer;
import org.project.by.booking.mapper.BookingMapper;
import org.project.by.booking.repository.BookingRepository;
import org.project.by.booking.service.BookingService;
import org.project.by.booking.service.DistanceService;
import org.project.by.booking.service.PricingService;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.project.by.common.constants.dto.event.BookingRequestEvent;
import org.project.by.common.constants.dto.event.CompletedRideEvent;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingProducer bookingProducer;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final DistanceService distanceService;

    private final PricingService pricingService;

    @Override
    @Transactional("transactionManager")
    public void processBookingRequest(BookingRequestEvent booking) {
        double distance = distanceService.calculateDistance(booking.getInitialLocation(),
                booking.getDestinationLocation());
        BigDecimal price = pricingService.calculatePrice(distance);
        booking.setDistance(distance);
        booking.setPrice(price);
        booking.setStatus(RideStatus.PENDING);

        Ride savedRide = bookingRepository.save(bookingMapper.fromDto(booking));
        booking.setRideId(savedRide.getId());
        bookingProducer.sendBooking(booking);
    }

    @Override
    public void updateRideStatus(BookingRequestEvent ride) {
        Ride rideEntity = this.findRideById(ride.getRideId());
        rideEntity.setDriverId(ride.getDriverId());
        rideEntity.setStatus(RideStatus.IN_PROGRESS);
        bookingRepository.save(rideEntity);
    }

    @Override
    public void completeRide(CompletedRideEvent rideCompleted) {
        Ride ride = this.findRideById(rideCompleted.getRideId());
        ride.setStatus(RideStatus.COMPLETED);
        bookingRepository.save(ride);
    }

    @Override
    public PageDto<RidesDto> getPassengerHistory(Long passengerId, Pageable pageable) {
        Page<Ride> rides = bookingRepository.findAllByPassengerId(passengerId, pageable);
        return bookingMapper.toRideDtoPage(rides);
    }

    @Override
    public PageDto<RidesDto> getDriverHistory(Long driverId, Pageable pageable) {
        Page<Ride> rides = bookingRepository.findAllByDriverId(driverId, pageable);
        return bookingMapper.toRideDtoPage(rides);
    }

    @Override
    @Transactional("transactionManager")
    public void cancelRide(RideEvent rideCancelledEvent) {
        Ride ride = this.findRideById(rideCancelledEvent.getRideId());
        if (ride.getStatus() == RideStatus.PENDING) {
            ride.setStatus(RideStatus.SUSPENDED);
            bookingRepository.save(ride);
        }
    }

    private Ride findRideById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
    }

}
