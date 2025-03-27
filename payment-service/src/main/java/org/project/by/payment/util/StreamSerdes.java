package org.project.by.payment.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.experimental.UtilityClass;
import org.springframework.kafka.support.serializer.JsonSerde;

@UtilityClass
public class StreamSerdes {

    public <T> JsonSerde<T> get(Class<T> clazz) {
        return new JsonSerde<>(clazz);
    }

    public <T> JsonSerde<T> getTypeReferenced() {
        return new JsonSerde<>(new TypeReference<>() {
        });
    }

}
