package org.project.by.common.constants.dto.event;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class RideEvent {

    @NotNull
    @Positive
    private Long rideId;

    @NotNull
    @Positive
    private Long driverId;

    @NotNull
    @Positive
    private Long passengerId;

    @Positive
    @NotNull
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;

}
