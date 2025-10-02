package com.rezerve.rezerveinventoryservice.mapper;

import com.rezerve.rezerveinventoryservice.dto.InventoryEventConsumerDto;
import com.rezerve.rezerveinventoryservice.dto.InventoryGrpcResponseDto;
import com.rezerve.rezerveinventoryservice.model.enums.EventCategory;
import event.events.EventCreatedKafkaEvent;
import event.events.EventDeletedKafkaEvent;
import event.events.EventSeatsUpdatedKafkaEvent;
import org.springframework.stereotype.Component;


@Component
public class InventoryMapper {

    public InventoryEventConsumerDto toInventoryEventConsumerDto(EventCreatedKafkaEvent event) {
        return new InventoryEventConsumerDto(
                event.getEventId(),
                event.getTotalSeats(),
                EventCategory.valueOf(event.getEventType())
        );
    }

    public InventoryEventConsumerDto toInventoryEventConsumerDto(EventSeatsUpdatedKafkaEvent event) {
        return new InventoryEventConsumerDto(
                event.getEventId(),
                event.getTotalSeats(),
                null
        );
    }

    public InventoryEventConsumerDto toInventoryEventConsumerDto(EventDeletedKafkaEvent event) {
        return new InventoryEventConsumerDto(
                event.getEventId(),
                null,
                null
        );
    }

    public InventoryGrpcResponseDto toInventoryGrpcResponseDto(Boolean seatBooked, String message) {

        InventoryGrpcResponseDto inventoryGrpcResponseDto = new InventoryGrpcResponseDto();

        inventoryGrpcResponseDto.setSeatBooked(seatBooked);
        inventoryGrpcResponseDto.setMessage(message);

        return inventoryGrpcResponseDto;
    }
}
