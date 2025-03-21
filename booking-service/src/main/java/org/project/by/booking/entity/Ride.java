package org.project.by.booking.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.project.by.common.constants.enums.RideStatus;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ride")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "passenger_id")
    private Long passengerId;

    @Column(name = "driver_id")
    private Long driverId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "longitude", column = @Column(name = "initial_long")),
            @AttributeOverride(name = "latitude", column = @Column(name = "initial_lat"))
    })
    private LocationEntity initialLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "longitude", column = @Column(name = "destination_long")),
            @AttributeOverride(name = "latitude", column = @Column(name = "destination_lat"))
    })
    private LocationEntity destinationLocation;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "price")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @CreatedDate
    @Column(name = "inserted_at", insertable = false, updatable = false)
    private LocalDateTime insertedAt;

    @Column(name = "completed_at", updatable = false)
    private LocalDateTime completedAt;

}
