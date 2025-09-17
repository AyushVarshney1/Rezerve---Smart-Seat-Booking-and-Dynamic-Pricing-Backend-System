package com.rezerve.rezerveeventservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// WE DON'T ALLOW CHANGING LOCATIONS OF EVENTS

@Getter
@Setter
public class EventUpdateRequestDto {

    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalSeats;
    private Double price;
}
