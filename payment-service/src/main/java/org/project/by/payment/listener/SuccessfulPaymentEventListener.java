package org.project.by.payment.listener;

import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.dto.event.SucceededPaymentEvent;
import org.project.by.payment.kafka.producer.PaymentProducer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SuccessfulPaymentEventListener {

    private final PaymentProducer paymentProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEntitySavedEvent(SucceededPaymentEvent event) {
        paymentProducer.sendPaymentSuccessful(event);
    }

}
