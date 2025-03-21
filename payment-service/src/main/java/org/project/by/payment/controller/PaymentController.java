package org.project.by.payment.controller;

import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.payment.dto.TransactionDto;
import org.project.by.payment.service.PaymentService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

//    private final KafkaTemplate<String, Object> template;

    private final PaymentService paymentService;

//    @PostMapping("/process")
//    public void process() {
//        CompletedRideEvent completedRideEvent = new CompletedRideEvent();
//        completedRideEvent.setRideId(1L);
//        completedRideEvent.setDriverId(1L);
//        completedRideEvent.setPassengerId(1L);
//        completedRideEvent.setPrice(new BigDecimal("25.50"));
//        Message<CompletedRideEvent> event = KafkaMessageTemplate
//                .payload(completedRideEvent)
//                .topic(KafkaConstants.COMPLETED_RIDES_TOPIC)
//                .build();
//        template.send(event);
//    }

    @GetMapping("/transactions/{id}")
    public PageDto<TransactionDto> findUserTransactions(@PathVariable("id") Long id,
                                                        Pageable pageable) {
        return paymentService.findUserTransactions(id, pageable);
    }

}
