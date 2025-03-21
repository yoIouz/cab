package org.project.by.payment.repository;

import jakarta.persistence.LockModeType;
import org.project.by.payment.entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserBalance> findByUserId(Long userId);

}
