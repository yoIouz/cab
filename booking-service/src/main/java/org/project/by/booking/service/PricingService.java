package org.project.by.booking.service;

import java.math.BigDecimal;

public interface PricingService {

    BigDecimal calculatePrice(double distance);

}
