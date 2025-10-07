package com.rezerve.rezerveinventoryservice.mapper;

import com.rezerve.rezerveinventoryservice.dto.InventoryEventConsumerDto;
import com.rezerve.rezerveinventoryservice.dto.InventoryGrpcResponseDto;
import com.rezerve.rezerveinventoryservice.dto.SeatBookingUpdatedDto;
import com.rezerve.rezerveinventoryservice.model.Inventory;
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

    public SeatBookingUpdatedDto toSeatBookingUpdatedDto(Inventory inventory) {
        SeatBookingUpdatedDto seatBookingUpdatedDto = new SeatBookingUpdatedDto();

        seatBookingUpdatedDto.setEventId(inventory.getEventId());
        seatBookingUpdatedDto.setTotalSeats(inventory.getTotalSeats());
        seatBookingUpdatedDto.setAvailableSeats(inventory.getAvailableSeats());
        seatBookingUpdatedDto.setEventCategory(inventory.getEventCategory());

        return seatBookingUpdatedDto;
    }
}
