package org.project.by.payment.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@TestConfiguration
public class NoKafkaConfiguration {

    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate() {
        return Mockito.mock(KafkaTemplate.class);
    }

}
