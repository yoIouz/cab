package org.project.by.payment.consumer;

import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.dto.event.CompletedRideEvent;
import org.project.by.common.constants.kafka.KafkaConstants;
import org.project.by.payment.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = KafkaConstants.COMPLETED_RIDES_TOPIC)
    public void consumeCompleted(CompletedRideEvent rideCompleted) {
        paymentService.processPayment(rideCompleted);
    }

//    @JmsListener(destination = "completed")
//    public void consumeCompleted(CompletedRideEvent rideCompleted) {
//        paymentService.processPayment(rideCompleted);
//    }

}
