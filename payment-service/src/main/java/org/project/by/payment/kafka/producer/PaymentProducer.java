package org.project.by.payment.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.dto.event.SucceededPaymentEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.project.by.common.constants.kafka.KafkaMessageTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentSuccessful(SucceededPaymentEvent paymentEvent) {
        Message<SucceededPaymentEvent> message = KafkaMessageTemplate.payload(paymentEvent)
                .topic(KafkaConstants.PAYMENT_TOPIC)
//                .header(KafkaHeaders.KEY, String.valueOf(paymentEvent.getId()))
                .build();
        kafkaTemplate.send(message);
    }

}
