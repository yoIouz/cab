package org.project.by.payment.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.project.by.payment.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = KafkaConstants.COMPLETED_RIDES_TOPIC)
    public void consumeCompleted(RideEvent rideCompleted) {
        paymentService.processPayment(rideCompleted);
    }

//    @JmsListener(destination = "completed")
//    public void consumeCompleted(RideEvent rideCompleted) {
//        paymentService.processPayment(rideCompleted);
//    }

}
