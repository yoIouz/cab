package org.project.by.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(

        Long userId,

        BigDecimal amount,

        LocalDateTime transactionDate

) {
}
