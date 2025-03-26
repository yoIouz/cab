package org.project.by.common.constants.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project.by.common.constants.dto.TransactionReferenceDto;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SucceededPaymentEvent {

    private Long id;

    private BigDecimal amount;

    private TransactionReferenceDto transaction;

}



