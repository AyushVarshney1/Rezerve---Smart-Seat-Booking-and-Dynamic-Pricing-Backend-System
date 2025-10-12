package com.rezerve.rezervebookingservice.repository;

import com.rezerve.rezervebookingservice.model.Booking;
import com.rezerve.rezervebookingservice.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    Optional<Booking> findByBookingId(UUID bookingId);

    List<Booking> findByCreatedDateLessThanAndBookingStatus(LocalDateTime createdDate, BookingStatus status);
}
