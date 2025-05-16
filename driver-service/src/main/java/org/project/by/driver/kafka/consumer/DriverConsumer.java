package org.project.by.driver.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.project.by.driver.service.DriverService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverConsumer {

    private final DriverService driverService;

    @KafkaListener(topics = KafkaConstants.BOOKING_RIDE_TOPIC)
    public void consume() {
        log.info("RECEIVED RIDE REQUEST");
    }

    @KafkaListener(topicPartitions =
    @TopicPartition(topic = KafkaConstants.RATING_TOPIC,
            partitions = KafkaConstants.DRIVER_RATING_PARTITION)
    )
    public void consume(UserRatingEvent record) {
        driverService.saveRating(record);
    }

}
