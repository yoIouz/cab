package org.project.by.common.constants.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.project.by.common.constants.dto.event.SucceededPaymentEvent;

@Getter
@Setter
@AllArgsConstructor
public class FraudReferenceDto {

    private SucceededPaymentEvent event;

    private long count;

}
