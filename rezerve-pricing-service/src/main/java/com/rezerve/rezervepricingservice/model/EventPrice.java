package com.rezerve.rezervepricingservice.model;

import com.rezerve.rezervepricingservice.model.enums.EventCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "event_prices")
public class EventPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private Long eventId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EventCategory eventCategory;

    @Column(nullable = false, updatable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Double currentPrice;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
