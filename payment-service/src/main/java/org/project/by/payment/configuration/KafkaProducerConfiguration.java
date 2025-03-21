package org.project.by.payment.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.admin.NewTopic;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public StringJsonMessageConverter stringJsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new StringJsonMessageConverter(objectMapper);
    }

    @Bean
    public NewTopic completedRides() {
        return TopicBuilder.name(KafkaConstants.COMPLETED_RIDES_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
