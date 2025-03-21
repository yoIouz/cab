package org.project.by.booking.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class LocationEntity {

    private double longitude;

    private double latitude;

}
