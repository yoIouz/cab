package org.project.by.common.constants.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project.by.common.constants.enums.RideStatus;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RidesDto {

    private Long id;

    private Long passengerId;

    private Long driverId;

    private LocalDate date;

    private RideStatus status;

}
