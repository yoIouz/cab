package org.project.by.payment;

import org.project.by.common.constants.dto.event.RideEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CompletedRideBuilder {

    private final RideEvent event;

    public CompletedRideBuilder(RideEvent event) {
        this.event = event;
    }

    public static CompletedRideBuilder createCompletedRideBuilder() {
        return new CompletedRideBuilder(new RideEvent());
    }

    public CompletedRideBuilder completedTime(LocalDateTime time) {
        event.setDate(time);
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

    public RideEvent build() {
        return event;
    }

}
