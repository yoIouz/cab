package org.project.by.booking.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class DistanceUtils {

    private final double M_TO_KM_CONSTANT = 1.609344;

    private final int PRECISION = 2;

    public double calculateDistance(double initialLat, double initialLon,
                                    double destinationLat, double destinationLon) {
        double theta = initialLon - destinationLon;
        double dist = Math.sin(deg2rad(initialLat)) * Math.sin(deg2rad(destinationLat)) +
                Math.cos(deg2rad(initialLat)) * Math.cos(deg2rad(destinationLat)) * Math.cos(deg2rad(theta));
        dist = rad2deg(Math.acos(dist));
        dist *= 60 * 1.1515;
        return scaleValue(milesToKm(dist));
    }

    private double deg2rad(double deg) {
        return Math.toRadians(deg);
    }

    private double rad2deg(double rad) {
        return Math.toDegrees(rad);
    }

    private double milesToKm(double value) {
        return value * M_TO_KM_CONSTANT;
    }

    private double scaleValue(double value) {
        return new BigDecimal(value)
                .setScale(PRECISION, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
