package com.rezerve.rezerveinventoryservice.kafka;

import booking.events.ReleaseSeatsEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import com.rezerve.rezerveinventoryservice.mapper.InventoryMapper;
import com.rezerve.rezerveinventoryservice.service.InventoryService;
import event.events.EventCreatedKafkaEvent;
import event.events.EventDeletedKafkaEvent;
import event.events.EventSeatsUpdatedKafkaEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventConsumer.class);
    private final InventoryService inventoryService;
    private final InventoryMapper inventoryMapper;

    @KafkaListener(topics = "event.created.topic.v1", groupId = "rezerve-inventory-service")
    public void consumeEventCreatedKafkaEvent(byte[] bytes){
        try{
            EventCreatedKafkaEvent  event = EventCreatedKafkaEvent.parseFrom(bytes);
            log.info("Received event created event from topic");

            inventoryService.createInventory(inventoryMapper.toInventoryEventConsumerDto(event));

        }catch(InvalidProtocolBufferException e){
            log.error("Error deserializing event created event {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "event.seats.updated.topic.v1", groupId = "rezerve-inventory-service")
    public void consumeEventSeatsUpdatedKafkaEvent(byte[] bytes){
        try{
            EventSeatsUpdatedKafkaEvent event = EventSeatsUpdatedKafkaEvent.parseFrom(bytes);
            log.info("Received event seats updated event from topic");

            inventoryService.updateEventSeats(inventoryMapper.toInventoryEventConsumerDto(event));

        }catch(InvalidProtocolBufferException e){
            log.error("Error deserializing event seats updated event {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "event.deleted.topic.v1", groupId = "rezerve-inventory-service")
    public void consumeEventDeletedKafkaEvent(byte[] bytes){
        try{
            EventDeletedKafkaEvent event = EventDeletedKafkaEvent.parseFrom(bytes);
            log.info("Received event deleted event from topic");

            inventoryService.deleteInventory(inventoryMapper.toInventoryEventConsumerDto(event));

        }catch(InvalidProtocolBufferException e){
            log.error("Error deserializing event deleted event {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "release.seat.topic.v1", groupId = "rezerve-inventory-service")
    public void consumeReleaseSeatKafkaEvent(byte[] bytes){
        try{
            ReleaseSeatsEvent event = ReleaseSeatsEvent.parseFrom(bytes);
            log.info("Received release seat event from topic");

            inventoryService.releaseSeat(event.getEventId(), event.getSeats());

        }catch(InvalidProtocolBufferException e){
            log.error("Error deserializing release seat event {}", e.getMessage());
        }
    }
}
