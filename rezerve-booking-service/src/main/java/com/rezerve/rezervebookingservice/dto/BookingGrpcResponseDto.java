package com.rezerve.rezervebookingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BookingGrpcResponseDto {

    private Boolean exists;
    private Double totalPrice;
    private String message;
}
