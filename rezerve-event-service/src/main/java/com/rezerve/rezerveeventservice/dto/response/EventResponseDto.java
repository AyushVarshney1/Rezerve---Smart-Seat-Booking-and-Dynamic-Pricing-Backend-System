package com.rezerve.rezerveeventservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rezerve.rezerveeventservice.model.enums.EventCategory;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // exclude null fields
public class EventResponseDto {

    private Long id;
    private String name;
    private EventCategory category;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalSeats;
    private Double price;
}


