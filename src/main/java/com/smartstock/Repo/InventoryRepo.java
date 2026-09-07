package com.smartstock.Repo;

import com.smartstock.model.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Inventory> findByProduct_Id(Long productId);
}