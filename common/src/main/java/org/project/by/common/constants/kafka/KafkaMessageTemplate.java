package org.project.by.common.constants.kafka;

import lombok.Getter;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Objects;

@Getter
public final class KafkaMessageTemplate<T> {

    private final MessageBuilder<T> innerMessageBuilder;

    public KafkaMessageTemplate(T payload) {
        this.innerMessageBuilder = MessageBuilder.withPayload(payload);
    }

    public KafkaMessageTemplate<T> header(String headerName, @Nullable Object headerValue) {
        this.innerMessageBuilder.setHeader(headerName, headerValue);
        return this;
    }

    public KafkaMessageTemplate<T> topic(@Nullable Object topic) {
        this.innerMessageBuilder.setHeader(KafkaHeaders.TOPIC, topic);
        return this;
    }

    public KafkaMessageTemplate<T> partition(@Nullable Object partition) {
        if (Objects.nonNull(partition) && !(partition instanceof Integer)) {
            partition = Integer.valueOf(String.valueOf(partition));
            this.innerMessageBuilder.setHeader(KafkaHeaders.PARTITION, partition);
        }
        return this;
    }

    public static <T> KafkaMessageTemplate<T> payload(T payload) {
        return new KafkaMessageTemplate<>(payload);
    }

    public Message<T> build() {
        return this.innerMessageBuilder.build();
    }

}
