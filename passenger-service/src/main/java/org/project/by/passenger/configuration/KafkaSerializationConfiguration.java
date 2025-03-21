package org.project.by.passenger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class KafkaSerializationConfiguration {

    @Bean
    public StringJsonMessageConverter stringJsonMessageConverter() {
        return new StringJsonMessageConverter();
    }

}
