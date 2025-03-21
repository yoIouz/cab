package org.project.by.driver.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@TestConfiguration
public class KafkaTestConfiguration {

    @Bean
    public StringJsonMessageConverter stringJsonMessageConverter() {
        return new StringJsonMessageConverter();
    }

}
