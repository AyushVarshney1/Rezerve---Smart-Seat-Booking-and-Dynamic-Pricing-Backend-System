package com.rezerve.rezerveinventoryservice.repository;

import com.rezerve.rezerveinventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    Optional<Inventory> findByEventId(Long eventId);

    void deleteByEventId(Long eventId);
}
