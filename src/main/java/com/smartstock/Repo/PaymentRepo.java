package com.smartstock.Repo;

import com.smartstock.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Long> {

    Optional<Payment> findByOrderId(Long orderId);
    Optional<Payment> findByIdempotencyKey(String IdempotencyKey);

}
