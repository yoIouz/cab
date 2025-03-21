package org.project.by.booking.service.impl;

import org.project.by.booking.service.DistanceService;
import org.project.by.booking.utils.DistanceUtils;
import org.project.by.common.constants.dto.Location;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DistanceServiceImpl implements DistanceService {

    @Override
    public double calculateDistance(Location initialLocation, Location destinationLocation) {
        if (Objects.isNull(initialLocation) || Objects.isNull(destinationLocation)) {
            throw new IllegalArgumentException("Location and destination are null");
        }
        double distance = DistanceUtils.calculateDistance(initialLocation.latitude(),
                initialLocation.longitude(),
                destinationLocation.latitude(),
                destinationLocation.longitude());
        if (distance <= 0.0) {
            throw new RuntimeException("Invalid distance");
        }
        return distance;
    }

}
