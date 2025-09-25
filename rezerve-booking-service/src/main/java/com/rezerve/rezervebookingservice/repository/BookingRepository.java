package com.rezerve.rezervebookingservice.repository;

import com.rezerve.rezervebookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    Optional<Booking> findByBookingId(UUID bookingId);
}
