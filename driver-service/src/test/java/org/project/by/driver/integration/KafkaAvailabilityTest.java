package org.project.by.driver.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.by.driver.SucceededWatcher;
import org.project.by.driver.testcontainers.KafkaTestContainer;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SucceededWatcher.class)
public class KafkaAvailabilityTest implements KafkaTestContainer {

    @Test
    void testKafkaAvailability() {
        assertThat(kafkaContainer.isRunning()).isTrue();
    }

}
