package com.rezerve.rezerveinventoryservice.repository;

import com.rezerve.rezerveinventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    Optional<Inventory> findByEventId(Long eventId);

    void deleteByEventId(Long eventId);

    @Modifying
    @Query("UPDATE Inventory i SET i.availableSeats = i.availableSeats - :seatsToBook " +
            "WHERE i.eventId = :eventId AND i.availableSeats >= :seatsToBook")
    int updateAvailableSeats(@Param("eventId") Long eventId,
                             @Param("seatsToBook") int seatsToBook);
}
