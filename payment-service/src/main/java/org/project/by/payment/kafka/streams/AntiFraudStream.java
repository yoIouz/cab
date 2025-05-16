package org.project.by.payment.kafka.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.WindowStore;
import org.project.by.common.constants.dto.FraudReferenceDto;
import org.project.by.common.constants.dto.TransactionReferenceDto;
import org.project.by.common.constants.dto.event.SucceededPaymentEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.project.by.payment.util.StreamSerdes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
@Profile("kafka")
@EnableKafkaStreams
public class AntiFraudStream {

    private final static String STORE_NAME = "fraud";

    private final static int IN_WINDOW_MAX_RECORDS = 1000;

    @Bean
    public KStream<String, SucceededPaymentEvent> kStream(StreamsBuilder kStreamBuilder) {
        KStream<String, SucceededPaymentEvent> sourceStream = kStreamBuilder
                .stream(KafkaConstants.PAYMENT_TOPIC, Consumed.with(Serdes.String(),
                        StreamSerdes.get(SucceededPaymentEvent.class)));

        KGroupedStream<String, SucceededPaymentEvent> groupedStream = sourceStream
                .filter((key, value) -> Objects.nonNull(value.getId()))
                .groupBy((key, value) -> value.getId().toString(),
                        Grouped.with(Serdes.String(), StreamSerdes.get(SucceededPaymentEvent.class)));

        KTable<Windowed<String>, List<TransactionReferenceDto>> aggregatedTable = groupedStream
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(1))
                        .advanceBy(Duration.ofSeconds(1)))
                .aggregate(
                        ArrayList::new,
                        (key, value, aggList) -> {
                            // FOR IMMUTABILITY
                            List<TransactionReferenceDto> updatedList = new ArrayList<>(aggList);
                            updatedList.add(value.getTransaction());
                            return updatedList;
                        },
                        Materialized.<String, List<TransactionReferenceDto>, WindowStore<Bytes, byte[]>>as(STORE_NAME)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(StreamSerdes.getTypeReferenced())
                                .withLoggingDisabled()
                );

        KStream<String, FraudReferenceDto> suspiciousPayments = aggregatedTable
                .suppress(Suppressed.untilWindowCloses(
                        Suppressed.BufferConfig.maxRecords(IN_WINDOW_MAX_RECORDS).shutDownWhenFull()
                ))
                .filter((key, value) ->
                        value.size() > 1)
                .toStream()
                .map((windowedKey, list) ->
                        KeyValue.pair(windowedKey.key(),
                                this.createFraudReferenceDto(windowedKey.key(), list)));

        suspiciousPayments.to(KafkaConstants.SUSPICIOUS_PAYMENT_TOPIC, Produced.with(Serdes.String(),
                StreamSerdes.getTypeReferenced()));

        return sourceStream;
    }

    private FraudReferenceDto createFraudReferenceDto(String key, List<TransactionReferenceDto> transactions) {
        return new FraudReferenceDto(key, transactions, transactions.size());
    }

}
