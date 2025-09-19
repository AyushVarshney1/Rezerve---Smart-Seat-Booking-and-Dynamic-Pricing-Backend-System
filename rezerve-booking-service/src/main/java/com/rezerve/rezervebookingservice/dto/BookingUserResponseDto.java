package com.rezerve.rezervebookingservice.dto;

import com.rezerve.rezervebookingservice.model.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class BookingUserResponseDto {

    private UUID bookingId;
    private Integer totalTickets;
    private Double totalPrice;
    private LocalDateTime createdDate;
    private String message = "Complete payment under 10 minutes to confirm booking";
}
