package org.project.by.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.dto.event.SucceededPaymentEvent;
import org.project.by.payment.dto.TransactionDto;
import org.project.by.payment.entity.Transaction;
import org.project.by.payment.entity.UserBalance;
import org.project.by.payment.mapper.PaymentMapper;
import org.project.by.payment.repository.TransactionRepository;
import org.project.by.payment.repository.UserBalanceRepository;
import org.project.by.payment.service.PaymentService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserBalanceRepository userBalanceRepository;

    private final TransactionRepository transactionRepository;

    private final PaymentMapper paymentMapper;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void processPayment(RideEvent event) {
        Long passengerId = event.getPassengerId();
        BigDecimal price = event.getPrice();
        UserBalance userBalance = userBalanceRepository.findByUserId(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userBalance.getBalance().compareTo(price) < 0) {
            throw new IllegalArgumentException("Insufficient funds for user " + passengerId);
        }

        userBalance.setBalance(userBalance.getBalance().subtract(price));
        Transaction transaction = new Transaction();
        transaction.setUserId(userBalance.getUserId());
        transaction.setAmount(price.negate());
        transaction.setTransactionDate(event.getDate());

        userBalanceRepository.save(userBalance);
        Transaction savedTx = transactionRepository.save(transaction);

        SucceededPaymentEvent succeededPaymentEvent =
                new SucceededPaymentEvent(passengerId, price, paymentMapper.toTransactionReferenceDto(savedTx));
        eventPublisher.publishEvent(succeededPaymentEvent);
    }

    @Override
    public PageDto<TransactionDto> findUserTransactions(Long id, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAllByUserId(id, pageable);
        return paymentMapper.toTransactionDtoPage(transactions);
    }

}
