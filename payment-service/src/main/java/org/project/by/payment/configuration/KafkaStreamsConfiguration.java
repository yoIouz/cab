package org.project.by.payment.configuration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.SlidingWindows;
import org.project.by.common.constants.dto.event.SucceededPaymentEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.Objects;

@Configuration
@Profile("kafka")
@EnableKafkaStreams
public class KafkaStreamsConfiguration {

    @Bean
    public KStream<String, SucceededPaymentEvent> kStream(StreamsBuilder kStreamBuilder) {
        try (JsonSerde<SucceededPaymentEvent> serde = new JsonSerde<>(SucceededPaymentEvent.class)) {
            KStream<String, SucceededPaymentEvent> stream = kStreamBuilder
                    .stream(KafkaConstants.PAYMENT_TOPIC, Consumed.with(Serdes.String(), serde));

            KGroupedStream<String, SucceededPaymentEvent> stream1 = stream
                    .filter((key, value) -> Objects.nonNull(value.getId()))
                    .selectKey((key, value) -> "userId:" + value.getId())
                    .groupByKey();

            stream1.windowedBy(SlidingWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(1)))
                    .count()
                    .toStream()
                    .map((windowedKey, count) -> KeyValue.pair(windowedKey.key(), count))
                    .to(KafkaConstants.SUSPICIOUS_PAYMENT_TOPIC, Produced.with(Serdes.String(), new JsonSerde<>(Long.class)));

            return stream;
        }
    }

}
