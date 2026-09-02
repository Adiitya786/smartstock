package com.smartstock.Repo;

import com.smartstock.model.Inventory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory,Long> {
    Optional<Inventory> findByProductId(Long productId);
}
