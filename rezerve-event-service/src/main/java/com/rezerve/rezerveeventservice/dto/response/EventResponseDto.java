package com.rezerve.rezerveeventservice.dto.response;

import com.rezerve.rezerveeventservice.model.enums.EventCategory;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventResponseDto {

    private Long id;
    private String name;
    private EventCategory category;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalSeats;
    private BigDecimal price;
}


