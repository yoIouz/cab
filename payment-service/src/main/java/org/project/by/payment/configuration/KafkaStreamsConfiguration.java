package org.project.by.payment.configuration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.WindowStore;
import org.project.by.common.constants.dto.event.SucceededPaymentEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.project.by.payment.util.FraudSerdes;
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

    private final static String KEY_PREFIX = "userId: ";

    private final static String STORE_NAME = "fraud";

    @Bean
    public KStream<String, SucceededPaymentEvent> kStream(StreamsBuilder kStreamBuilder) {
        JsonSerde<Long> longJsonSerde = FraudSerdes.get(Long.class);
        KStream<String, SucceededPaymentEvent> sourceStream = kStreamBuilder
                .stream(KafkaConstants.PAYMENT_TOPIC, Consumed.with(Serdes.String(),
                        FraudSerdes.get(SucceededPaymentEvent.class)));

        KGroupedStream<String, SucceededPaymentEvent> groupedById = sourceStream
                .filter((key, value) -> Objects.nonNull(value.getId()))
                .groupBy((key, value) -> KEY_PREFIX + value.getId());

        groupedById.windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(1))
                        .advanceBy(Duration.ofSeconds(1)))
                .count(Materialized.<String, Long, WindowStore<Bytes, byte[]>>as(STORE_NAME)
                        .withKeySerde(Serdes.String())
                        .withValueSerde(longJsonSerde)
                        .withLoggingDisabled())
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .filter((key, value) -> value > 1)
                .toStream()
                .map((windowedKey, count) ->
                        KeyValue.pair(windowedKey.key(), count))
                .to(KafkaConstants.SUSPICIOUS_PAYMENT_TOPIC, Produced.with(Serdes.String(), longJsonSerde));

        return sourceStream;
    }

}
