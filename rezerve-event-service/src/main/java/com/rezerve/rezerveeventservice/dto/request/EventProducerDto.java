package com.rezerve.rezerveeventservice.dto.request;

import com.rezerve.rezerveeventservice.model.enums.EventCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventProducerDto {

    private Long eventId;

    // REQUIRED FOR EVENT CREATED & SEATS UPDATED EVENT
    private Integer totalSeats;

    // REQUIRED FOR EVENT CREATED EVENT
    private EventCategory eventCategory;
}
