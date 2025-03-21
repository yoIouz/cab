package org.project.by.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.payment.dto.TransactionDto;
import org.project.by.payment.entity.Transaction;
import org.springframework.data.domain.Page;

@Mapper
public interface PaymentMapper {

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "transactionDate", target = "transactionDate")
    TransactionDto toTransactionDto(Transaction transaction);

    default PageDto<TransactionDto> toTransactionDtoPage(Page<Transaction> transactions) {
        return new PageDto<>(transactions.map(this::toTransactionDto));
    }

}
