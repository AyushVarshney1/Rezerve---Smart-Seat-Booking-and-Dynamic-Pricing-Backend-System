package com.rezerve.rezerveeventservice.dto.request;

import com.rezerve.rezerveeventservice.model.enums.EventCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventRequestDto {

    @NotBlank(message = "Event name is required")
    private String name;
    @NotNull(message = "Category is required")
    private EventCategory category;
    private String description;
    @NotNull(message = "Start Time is required")
    private LocalDateTime startTime;
    @NotNull(message = "End Time is required")
    private LocalDateTime endTime;
    @NotNull(message = "Total Seats number is required")
    @Min(1)
    private Integer totalSeats;
    @NotNull(message = "Price name is required")
    @Min(1)
    private Double price;
}
