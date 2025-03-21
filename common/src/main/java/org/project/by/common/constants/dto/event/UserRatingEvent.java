package org.project.by.common.constants.dto.event;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserRatingEvent(

        @NotNull
        Long id,

        @Min(0)
        @Max(5)
        Integer rating

) {
}
