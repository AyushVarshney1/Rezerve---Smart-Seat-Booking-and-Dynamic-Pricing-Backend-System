package com.rezerve.rezerveinventoryservice.dto;

import com.rezerve.rezerveinventoryservice.model.enums.EventCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class InventoryEventConsumerDto {

    private Long eventId;
    private Integer totalSeats;
    private EventCategory eventCategory;
}
