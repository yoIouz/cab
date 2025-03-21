package org.project.by.booking.service.impl;

import org.project.by.booking.service.PricingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricingServiceImpl implements PricingService {

    private static final BigDecimal BASE_COST = new BigDecimal("3.00");

    private static final BigDecimal PER_KILOMETER = new BigDecimal("2.50");

    @Override
    public BigDecimal calculatePrice(double distance) {
        return BigDecimal.valueOf(distance)
                .multiply(BASE_COST)
                .multiply(PER_KILOMETER)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
