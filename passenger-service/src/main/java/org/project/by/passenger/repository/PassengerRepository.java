package org.project.by.passenger.repository;

import jakarta.persistence.LockModeType;
import org.project.by.passenger.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Modifying(clearAutomatically = true)
    @Query("update Passenger p set p.totalRatingCount = p.totalRatingCount + 1, " +
            "p.rating = (p.rating + :rating) / (p.totalRatingCount + 1) " +
            "where p.id = :id")
    void updateRating(@Param("id") Long id, @Param("rating") Integer rating);

}
