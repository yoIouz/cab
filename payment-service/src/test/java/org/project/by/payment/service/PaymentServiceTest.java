package org.project.by.payment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.event.RideEvent;
import org.project.by.common.constants.dto.event.SucceededPaymentEvent;
import org.project.by.payment.CompletedRideBuilder;
import org.project.by.payment.SucceededWatcher;
import org.project.by.payment.dto.TransactionDto;
import org.project.by.payment.entity.Transaction;
import org.project.by.payment.entity.UserBalance;
import org.project.by.payment.mapper.PaymentMapper;
import org.project.by.payment.repository.TransactionRepository;
import org.project.by.payment.repository.UserBalanceRepository;
import org.project.by.payment.service.impl.PaymentServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class, SucceededWatcher.class})
class PaymentServiceTest {

    @Mock
    UserBalanceRepository userBalanceRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentMapper paymentMapper;

    @Test
    void shouldDeductBalanceAndSaveTransaction_WhenUserHasSufficientFunds() {
        Long passengerId = 1L;
        BigDecimal price = new BigDecimal("100.00");
        LocalDateTime completedTime = LocalDateTime.now();

        RideEvent event = CompletedRideBuilder.createCompletedRideBuilder()
                .passengerId(passengerId)
                .completedTime(completedTime)
                .price(price)
                .build();
        UserBalance userBalance = new UserBalance(1L, passengerId, new BigDecimal("500.00"));

        when(userBalanceRepository.findByUserId(passengerId)).thenReturn(Optional.of(userBalance));

        paymentService.processPayment(event);

        assertThat(userBalance.getBalance()).isEqualTo(new BigDecimal("400.00"));
        verify(eventPublisher).publishEvent(any(SucceededPaymentEvent.class));
        verify(userBalanceRepository).save(userBalance);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldThrowException_WhenUserNotFound() {
        Long passengerId = 1L;
        BigDecimal price = new BigDecimal("100.00");
        LocalDateTime completedTime = LocalDateTime.now();
        RideEvent event = CompletedRideBuilder.createCompletedRideBuilder()
                .passengerId(passengerId)
                .completedTime(completedTime)
                .price(price)
                .build();

        when(userBalanceRepository.findByUserId(passengerId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> paymentService.processPayment(event))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");

        verify(userBalanceRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldThrowException_WhenInsufficientFunds() {
        Long passengerId = 1L;
        BigDecimal price = new BigDecimal("600.00");
        LocalDateTime completedTime = LocalDateTime.now();
        RideEvent event = CompletedRideBuilder.createCompletedRideBuilder()
                .passengerId(passengerId)
                .completedTime(completedTime)
                .price(price)
                .build();
        UserBalance userBalance = new UserBalance(1L, passengerId, new BigDecimal("500.00"));

        when(userBalanceRepository.findByUserId(passengerId)).thenReturn(Optional.of(userBalance));
        assertThatThrownBy(() -> paymentService.processPayment(event))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Insufficient funds for user " + passengerId);
        verify(userBalanceRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldReturnPagedTransactions() {
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();
        List<Transaction> transactions = List.of(
                new Transaction(1L, userId, BigDecimal.valueOf(100), LocalDateTime.now()),
                new Transaction(2L, userId, BigDecimal.valueOf(50), LocalDateTime.now())
        );
        Page<Transaction> transactionPage = new PageImpl<>(transactions, pageable, transactions.size());
        PageDto<TransactionDto> expectedPageDto = new PageDto<>(
                List.of(new TransactionDto(userId, BigDecimal.valueOf(100), LocalDateTime.now()),
                        new TransactionDto(userId, BigDecimal.valueOf(50), LocalDateTime.now())),
                10, 2, 1, 2
        );

        when(transactionRepository.findAllByUserId(userId, pageable)).thenReturn(transactionPage);
        when(paymentMapper.toTransactionDtoPage(transactionPage)).thenReturn(expectedPageDto);

        PageDto<TransactionDto> result = paymentService.findUserTransactions(userId, pageable);
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        verify(transactionRepository).findAllByUserId(userId, pageable);
        verify(paymentMapper).toTransactionDtoPage(transactionPage);
    }

}
