package org.project.by.payment;

import org.project.by.common.constants.dto.event.CompletedRideEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CompletedRideBuilder {

    private final CompletedRideEvent event;

    public CompletedRideBuilder(CompletedRideEvent event) {
        this.event = event;
    }

    public static CompletedRideBuilder createCompletedRideBuilder() {
        return new CompletedRideBuilder(new CompletedRideEvent());
    }

    public CompletedRideBuilder completedTime(LocalDateTime time) {
        event.setCompletedTime(time);
        return this;
    }

    public CompletedRideBuilder passengerId(Long passengerId) {
        event.setPassengerId(passengerId);
        return this;
    }

    public CompletedRideBuilder price(BigDecimal price) {
        event.setPrice(price);
        return this;
    }

    public CompletedRideEvent build() {
        return event;
    }

}
