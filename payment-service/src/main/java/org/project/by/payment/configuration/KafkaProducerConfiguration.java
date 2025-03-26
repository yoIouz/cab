package org.project.by.payment.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder.name(KafkaConstants.PAYMENT_TOPIC)
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
    public NewTopic successPayment() {
        return TopicBuilder.name(KafkaConstants.SUSPICIOUS_PAYMENT_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
