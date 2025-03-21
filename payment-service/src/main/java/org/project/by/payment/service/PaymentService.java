package org.project.by.payment.service;

import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.event.CompletedRideEvent;
import org.project.by.payment.dto.TransactionDto;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    void processPayment(CompletedRideEvent payment);

    PageDto<TransactionDto> findUserTransactions(Long id, Pageable pageable);

}
