package org.project.by.booking.repository;

import jakarta.persistence.QueryHint;
import org.project.by.booking.entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Ride, Long> {

    @QueryHints(@QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Page<Ride> findAllByDriverId(Long driverId, Pageable pageable);

    @QueryHints(@QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Page<Ride> findAllByPassengerId(Long passengerId, Pageable pageable);

}
