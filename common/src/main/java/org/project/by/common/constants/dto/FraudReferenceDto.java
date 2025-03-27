package org.project.by.common.constants.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FraudReferenceDto {

    private String userId;

    private List<TransactionReferenceDto> events;

    private long count;

}
