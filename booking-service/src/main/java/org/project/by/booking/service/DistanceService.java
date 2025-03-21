package org.project.by.booking.service;

import org.project.by.common.constants.dto.Location;

public interface DistanceService {

    double calculateDistance(Location initialLocation, Location destinationLocation);

}
