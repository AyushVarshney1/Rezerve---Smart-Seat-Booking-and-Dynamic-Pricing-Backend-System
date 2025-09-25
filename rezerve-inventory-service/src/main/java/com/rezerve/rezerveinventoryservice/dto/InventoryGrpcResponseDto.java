package com.rezerve.rezerveinventoryservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryGrpcResponseDto {

    private Boolean seatBooked;
    private String message;
}
