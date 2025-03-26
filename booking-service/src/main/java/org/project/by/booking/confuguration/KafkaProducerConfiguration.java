package org.project.by.booking.confuguration;

import org.apache.kafka.clients.admin.NewTopic;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public NewTopic requestTopic() {
        return TopicBuilder.name(KafkaConstants.REQUEST_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic bookingTopic() {
        return TopicBuilder.name(KafkaConstants.BOOKING_RIDE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic acceptedRideTopic() {
        return TopicBuilder.name(KafkaConstants.ACCEPTED_RIDES_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic completedRides() {
        return TopicBuilder.name(KafkaConstants.COMPLETED_RIDES_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic cancelledRides() {
        return TopicBuilder.name(KafkaConstants.CANCELLED_RIDES_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder.name(KafkaConstants.PAYMENT_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ratingTopic() {
        return TopicBuilder.name(KafkaConstants.RATING_TOPIC)
                .partitions(2)
                .replicas(1)
                .build();
    }

}
