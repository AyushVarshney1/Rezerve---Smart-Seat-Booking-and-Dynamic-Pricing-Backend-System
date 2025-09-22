package com.rezerve.rezervebookingservice.dto;

import com.rezerve.rezervebookingservice.model.enums.EventCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventServiceGrpcResponseDto {

    private Double price;
    private String eventName;
    private EventCategory eventCategory;

}
