package com.rezerve.rezervebookingservice.model;

import com.rezerve.rezervebookingservice.model.enums.BookingStatus;
import com.rezerve.rezervebookingservice.model.enums.EventCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID bookingId;

    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private Long eventId;

    @Column(nullable = false, updatable = false)
    private String eventName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EventCategory eventCategory;

    @Column(nullable = false, updatable = false)
    private Integer totalTickets;

    @Column(nullable = false, updatable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @CreatedDate
    private LocalDateTime createdDate;

    @PrePersist
    public void generateBookingId() {
        if (bookingId == null) {
            bookingId = UUID.randomUUID();
        }
    }
}
