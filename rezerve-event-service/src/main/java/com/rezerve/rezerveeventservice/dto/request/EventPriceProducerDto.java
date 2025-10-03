package com.rezerve.rezerveeventservice.dto.request;

import com.rezerve.rezerveeventservice.model.enums.EventCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventPriceProducerDto {

    private Long eventId;
    private Double price;
    private EventCategory eventCategory;
    private Integer totalSeats;
}
