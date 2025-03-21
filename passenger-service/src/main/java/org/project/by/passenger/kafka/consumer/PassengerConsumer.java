package org.project.by.passenger.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.project.by.passenger.service.PassengerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PassengerConsumer {

    private final PassengerService passengerService;

    @KafkaListener(topics = KafkaConstants.COMPLETED_RIDES_TOPIC)
    public void consumeCompleted(RideEvent rideCompleted) {
        passengerService.acknowledgeCompleted(rideCompleted);
        log.info("RIDE COMPLETED");
    }

    @KafkaListener(topics = KafkaConstants.CANCELLED_RIDES_TOPIC)
    public void consumeCancelled(RideEvent rideCancelled) {
        passengerService.acknowledgeCancelled(rideCancelled);
        log.info("RIDE CANCELLED");
    }

    @KafkaListener(topicPartitions =
    @TopicPartition(topic = KafkaConstants.RATING_TOPIC,
            partitions = KafkaConstants.PASSENGER_RATING_PARTITION)
    )
    public void consumeRating(UserRatingEvent record) {
        passengerService.saveRating(record);
    }

}
