//package com.rezerve.rezerveeventservice.model;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//
//import java.util.Date;
//import java.util.EventListener;
//
//@Getter
//@Setter
//@Entity
//@EntityListeners(EventListener.class)
//@Table(name = "events")
//public class Event {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, updatable = false)
//    private String category;
//
//    @Column(length = 500)
//    private String description;
//
//    @Column
//    private String fromLocation;
//
//    @Column(nullable = false)
//    private Date startTime;
//
//    @Column(nullable = false)
//    private Date endTime;
//
//    @Column(nullable = false)
//    private Integer totalSeats;
//
//    @Column(nullable = false)
//    private Double price;
//
//    @CreatedDate
//    @Column(nullable = false, updatable = false)
//    private Date createdAt;
//
//    @LastModifiedDate
//    @Column(nullable = false)
//    private Date updatedAt;
//}
