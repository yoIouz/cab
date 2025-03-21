package org.project.by.payment.testcontainers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public interface KafkaTestContainer {

    DockerImageName CAB_KAFKA =
            DockerImageName.parse("confluentinc/cp-kafka:7.4.0");

    @Container
    ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer(CAB_KAFKA);

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("KAFKA_BOOTSTRAP_SERVERS", kafkaContainer::getBootstrapServers);
    }

    default String getBootstrapServers() {
        return kafkaContainer.getBootstrapServers();
    }

}
