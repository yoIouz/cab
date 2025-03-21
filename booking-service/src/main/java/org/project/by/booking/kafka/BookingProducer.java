package org.project.by.booking.kafka;

import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.dto.event.BookingRequestEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.project.by.common.constants.kafka.KafkaMessageTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingProducer {

    private final KafkaTemplate<String, BookingRequestEvent> kafkaTemplate;

    public void sendBooking(BookingRequestEvent bookingRequestEvent) {
        Message<BookingRequestEvent> message = KafkaMessageTemplate.payload(bookingRequestEvent)
                .topic(KafkaConstants.BOOKING_RIDE_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

}
