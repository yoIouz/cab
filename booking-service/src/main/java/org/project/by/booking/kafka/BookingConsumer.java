package org.project.by.booking.kafka;

import lombok.RequiredArgsConstructor;
import org.project.by.booking.service.BookingService;
import org.project.by.common.constants.dto.event.BookingRequestEvent;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingConsumer {

    private final BookingService bookingService;

    @KafkaListener(topics = KafkaConstants.REQUEST_TOPIC)
    public void consume(BookingRequestEvent bookingRequestEvent) {
        bookingService.processBookingRequest(bookingRequestEvent);
    }

    @KafkaListener(topics = KafkaConstants.ACCEPTED_RIDES_TOPIC)
    public void consumeAcceptedRides(RideEvent ride) {
        bookingService.updateRideStatus(ride);
    }

    @KafkaListener(topics = KafkaConstants.COMPLETED_RIDES_TOPIC)
    public void consumeCompletedRides(RideEvent rideCompleted) {
        bookingService.completeRide(rideCompleted);
    }

}
