package com.rezerve.rezervebookingservice.dto;

import com.rezerve.rezervebookingservice.model.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class BookingAdminResponseDto {

    private Long id;
    private UUID bookingId;
    private Long userId;
    private Long eventId;
    private Integer totalTickets;
    private Double totalPrice;
    private BookingStatus bookingStatus;
    private LocalDateTime createdDate;

}
