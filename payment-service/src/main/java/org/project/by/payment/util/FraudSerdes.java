package org.project.by.payment.util;

import lombok.experimental.UtilityClass;
import org.springframework.kafka.support.serializer.JsonSerde;

@UtilityClass
public class FraudSerdes {

    public <T> JsonSerde<T> get(Class<T> clazz) {
        return new JsonSerde<>(clazz);
    }

}
