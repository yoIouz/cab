package org.project.by.passenger.testcontainers;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewPartitions;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.Properties;

@Testcontainers
public interface KafkaTestContainer {

    DockerImageName CAB_KAFKA =
            DockerImageName.parse("confluentinc/cp-kafka:7.4.0");

    @Container
    ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer(CAB_KAFKA)
            .waitingFor(Wait.forListeningPort());

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("KAFKA_BOOTSTRAP_SERVERS", kafkaContainer::getBootstrapServers);
    }

    default String getBootstrapServers() {
        return kafkaContainer.getBootstrapServers();
    }

    default void changePartitionQuantity(String topicName, int totalCount) {
        Properties adminProps = new Properties();
        adminProps.put("bootstrap.servers", getBootstrapServers());
        try (AdminClient adminClient = AdminClient.create(adminProps)) {
            adminClient.createPartitions(Collections.singletonMap(topicName, NewPartitions.increaseTo(totalCount)));
        }
    }

}
