package com.rezerve.rezerveinventoryservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSeatInfo {

    private int availableSeats;
    private int totalSeats;
}
