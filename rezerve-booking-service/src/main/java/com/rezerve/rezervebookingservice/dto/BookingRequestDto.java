package com.rezerve.rezervebookingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequestDto {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Total ticket quantity to book is required")
    private Integer totalTickets;
}
