package org.project.by.passenger.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.kafka.KafkaMessageTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassengerProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T> void send(T payload, String topic, String partition) {
        Message<T> message = KafkaMessageTemplate.payload(payload)
                .topic(topic)
                .partition(partition)
                .build();
        kafkaTemplate.send(message);
    }

}

