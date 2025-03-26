package org.project.by.payment.service;

import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.payment.dto.TransactionDto;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    void processPayment(RideEvent payment);

    PageDto<TransactionDto> findUserTransactions(Long id, Pageable pageable);

}
