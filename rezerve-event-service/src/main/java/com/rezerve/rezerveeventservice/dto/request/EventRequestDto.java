package com.rezerve.rezerveeventservice.dto.request;

import com.rezerve.rezerveeventservice.model.enums.EventCategory;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventRequestDto {

    @NotBlank(message = "Event name is required")
    private String name;
    @NotBlank(message = "Category is required")
    private EventCategory category;
    private String description;
    @NotBlank(message = "Start Time is required")
    private LocalDateTime startTime;
    @NotBlank(message = "End Time is required")
    private LocalDateTime endTime;
    @NotBlank(message = "Total Seats number is required")
    private Integer totalSeats;
    @NotBlank(message = "Price name is required")
    private Double price;
}
