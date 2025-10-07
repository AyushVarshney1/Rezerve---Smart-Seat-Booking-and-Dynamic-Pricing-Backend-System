package com.rezerve.rezerveinventoryservice.dto;

import com.rezerve.rezerveinventoryservice.model.enums.EventCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatBookingUpdatedDto {

    private Long eventId;
    private Integer availableSeats;
    private Integer totalSeats;
    private EventCategory eventCategory;
}
