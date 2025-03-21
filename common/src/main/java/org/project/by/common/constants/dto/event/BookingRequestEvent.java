package org.project.by.common.constants.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project.by.common.constants.dto.Location;
import org.project.by.common.constants.enums.RideStatus;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestEvent {

    @NotNull
    private Long passengerId;

    @NotNull
    private Location initialLocation;

    @NotNull
    private Location destinationLocation;

    @Setter
    private Long driverId;

    @Setter
    private Double distance;

    @Setter
    private RideStatus status;

    @Setter
    private Long rideId;

    @Setter
    private BigDecimal price;

}
