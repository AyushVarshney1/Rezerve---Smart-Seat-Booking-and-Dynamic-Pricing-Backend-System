package com.rezerve.rezerveinventoryservice.mapper;

import com.rezerve.rezerveinventoryservice.dto.InventoryEventConsumerDto;
import event.events.EventCreatedKafkaEvent;
import event.events.EventDeletedKafkaEvent;
import event.events.EventSeatsUpdatedKafkaEvent;
import org.springframework.stereotype.Component;


@Component
public class InventoryMapper {

    public InventoryEventConsumerDto toInventoryEventConsumerDto(EventCreatedKafkaEvent event) {
        return new InventoryEventConsumerDto(
                event.getEventId(),
                event.getTotalSeats()
        );
    }

    public InventoryEventConsumerDto toInventoryEventConsumerDto(EventSeatsUpdatedKafkaEvent event) {
        return new InventoryEventConsumerDto(
                event.getEventId(),
                event.getTotalSeats()
        );
    }

    public InventoryEventConsumerDto toInventoryEventConsumerDto(EventDeletedKafkaEvent event) {
        return new InventoryEventConsumerDto(
                event.getEventId(),
                null
        );
    }
}
