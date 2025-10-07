package com.rezerve.rezerveinventoryservice.service;

import com.rezerve.rezerveinventoryservice.dto.SeatBookedEvent;
import com.rezerve.rezerveinventoryservice.kafka.InventoryKafkaProducer;
import com.rezerve.rezerveinventoryservice.mapper.InventoryMapper;
import com.rezerve.rezerveinventoryservice.model.Inventory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class AsyncKafkaService {

    private static final Logger log = LoggerFactory.getLogger(AsyncKafkaService.class);

    private final InventoryKafkaProducer inventoryKafkaProducer;
    private final InventoryMapper inventoryMapper;

    @Async("kafkaExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendKafkaAsyncEvent(SeatBookedEvent seatBookedEvent) {
        Inventory inventory = seatBookedEvent.getInventory();

        try{
            inventoryKafkaProducer.sendSeatBookingUpdatedEvent(inventoryMapper.toSeatBookingUpdatedDto(inventory));
            log.info("Thread: {} - Sent Seat Booking Event to kafka", Thread.currentThread().getName());
        }catch (Exception e) {
            log.error("Failed to send Inventory Kafka events for event ID: {}", inventory.getEventId(), e);
        }
    }
}
