package org.project.by.passenger.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.by.common.constants.dto.Location;
import org.project.by.common.constants.dto.event.BookingRequestEvent;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.project.by.passenger.SucceededWatcher;
import org.project.by.passenger.dto.PassengerDto;
import org.project.by.passenger.testcontainers.KafkaTestContainer;
import org.project.by.passenger.testcontainers.PostgresTestContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SucceededWatcher.class)
public class PassengerControllerIntegrationTest implements KafkaTestContainer, PostgresTestContainer {

    private static final String API_URL = "/api/passenger";

    private static ObjectMapper objectMapper;

    private static Map<String, Object> consumerProperties;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        consumerProperties =
                KafkaTestUtils.consumerProps(kafkaContainer.getBootstrapServers(), "test-consumer");
        consumerProperties.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
    }

    @Test
    @Transactional
    void shouldEditPassenger() throws Exception {
        PassengerDto dto = new PassengerDto(1L, "John",
                "Doe", "john@doe.com");
        mockMvc.perform(patch(API_URL.concat("/edit"))
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void shouldReturnPassenger() throws Exception {
        Long id = 1L;
        mockMvc.perform(get(API_URL.concat("/{id}"), id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Иван"));
    }

    @Test
    void shouldRateDriver() throws Exception {
        changePartitionQuantity(KafkaConstants.RATING_TOPIC, 2);
        UserRatingEvent userRatingEvent = new UserRatingEvent(1L, 3);
        mockMvc.perform(post(API_URL.concat("/rate"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRatingEvent)))
                .andExpect(status().isOk());

        try (Consumer<String, String> consumer = new KafkaConsumer<>(consumerProperties)) {
            consumer.assign(Collections.singleton(new TopicPartition(KafkaConstants.RATING_TOPIC,
                    Integer.parseInt(KafkaConstants.DRIVER_RATING_PARTITION))));
            ConsumerRecord<String, String> record =
                    KafkaTestUtils.getSingleRecord(consumer, KafkaConstants.RATING_TOPIC);
            UserRatingEvent value = objectMapper.readValue(record.value(), UserRatingEvent.class);

            assertThat(value).usingRecursiveComparison().isEqualTo(userRatingEvent);
        }
    }

    @Test
    void shouldReturnBadRequestWhenRatingExceedsMax() throws Exception {
        UserRatingEvent userRatingEvent = new UserRatingEvent(1L, 6);
        mockMvc.perform(post(API_URL.concat("/rate"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRatingEvent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenIdIsNull() throws Exception {
        UserRatingEvent userRatingEvent = new UserRatingEvent(null, 3);
        mockMvc.perform(post(API_URL.concat("/rate"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRatingEvent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenRatingIsNegative() throws Exception {
        UserRatingEvent userRatingEvent = new UserRatingEvent(1L, -1);
        mockMvc.perform(post(API_URL.concat("/rate"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRatingEvent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateOrder() throws Exception {
        BookingRequestEvent dto = new BookingRequestEvent(
                1L, new Location(0.0, 0.0), new Location(0.0, 0.0),
                1L, null, null, null, null);
        mockMvc.perform(post(API_URL.concat("/create"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        try (Consumer<String, String> consumer = new KafkaConsumer<>(consumerProperties)) {
            consumer.subscribe(Collections.singleton(KafkaConstants.REQUEST_TOPIC));
            ConsumerRecord<String, String> record =
                    KafkaTestUtils.getSingleRecord(consumer, KafkaConstants.REQUEST_TOPIC);
            BookingRequestEvent value = objectMapper.readValue(record.value(), BookingRequestEvent.class);

            assertThat(value).usingRecursiveComparison().isEqualTo(dto);
        }
    }

    @Test
    void shouldReturnBadRequestWhenEventPassengerIdIsNull() throws Exception {
        BookingRequestEvent dto = new BookingRequestEvent(
                null, new Location(0.0, 0.0), new Location(0.0, 0.0),
                null, null, null, null, null);
        mockMvc.perform(post(API_URL.concat("/create"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenInitLocationIsNull() throws Exception {
        BookingRequestEvent dto = new BookingRequestEvent(
                1L, null, new Location(0.0, 0.0),
                null, null, null, null, null);
        mockMvc.perform(post(API_URL.concat("/create"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDestLocationIsNull() throws Exception {
        BookingRequestEvent dto = new BookingRequestEvent(
                1L, new Location(0.0, 0.0), null,
                null, null, null, null, null);
        mockMvc.perform(post(API_URL.concat("/create"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

}
