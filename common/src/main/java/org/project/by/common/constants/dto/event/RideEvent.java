package org.project.by.common.constants.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSSS]")
    private LocalDateTime date = LocalDateTime.now();

}
