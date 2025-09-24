package com.rezerve.rezerveeventservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventProducerDto {

    private Long eventId;

    // REQUIRED FOR EVENT CREATED & SEATS UPDATED EVENT
    private Integer totalSeats;
}
