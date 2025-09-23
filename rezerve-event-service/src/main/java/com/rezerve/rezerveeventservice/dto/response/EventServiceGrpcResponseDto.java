package com.rezerve.rezerveeventservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventServiceGrpcResponseDto {

    private Boolean exists;
    private Double price;
    private String eventName;
    private String eventCategory;
    private String message;
}
