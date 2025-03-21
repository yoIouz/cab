package org.project.by.driver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "driver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "driver")
    private DriverStatus isBusy;

    @Column(name = "name")
    private String name;

    @Column(name = "car")
    private String car;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "total_rating_count")
    private Integer totalRatingCount;

    @CreatedDate
    @Column(name = "inserted_at", insertable = false, updatable = false)
    private LocalDateTime insertedAt;

}
