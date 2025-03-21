package org.project.by.passenger.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.by.passenger.testcontainers.KafkaTestContainer;
import org.project.by.passenger.SucceededWatcher;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SucceededWatcher.class)
public class KafkaAvailabilityTest implements KafkaTestContainer {

    @Test
    void testKafkaAvailability() {
        assertThat(getBootstrapServers()).isNotBlank();
    }

}
