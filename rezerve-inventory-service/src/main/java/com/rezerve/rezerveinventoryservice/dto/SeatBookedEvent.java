package com.rezerve.rezerveinventoryservice.dto;

import com.rezerve.rezerveinventoryservice.model.Inventory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SeatBookedEvent {

    private final Inventory inventory;
}
