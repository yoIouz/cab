package org.project.by.common.constants.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AcceptedRideEvent extends RideEvent {

    private LocalDateTime acceptanceTime = LocalDateTime.now();

}