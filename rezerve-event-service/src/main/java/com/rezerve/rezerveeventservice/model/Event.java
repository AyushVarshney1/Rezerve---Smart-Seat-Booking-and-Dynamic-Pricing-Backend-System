package com.rezerve.rezerveeventservice.model;

import com.rezerve.rezerveeventservice.model.enums.EventCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EventCategory category;

    @Column(length = 500)
    private String description;

    // FOR TRAVEL-BASED EVENTS
    @Column(nullable = true)
    private String fromLocation;

    // FOR TRAVEL-BASED EVENTS
    @Column(nullable = true)
    private String toLocation;

    // FOR VENUE-BASED EVENTS
    @Column(nullable = true)
    private String venueLocation;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Double price;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
