package com.rezerve.rezerveinventoryservice.kafka;

import com.rezerve.rezerveinventoryservice.dto.SeatBookingUpdatedDto;
import inventory.events.SeatBookingUpdated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(InventoryKafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendSeatBookingUpdatedEvent(SeatBookingUpdatedDto seatBookingUpdatedDto) {
        SeatBookingUpdated seatBookingUpdated = SeatBookingUpdated.newBuilder()
                .setEventId(seatBookingUpdatedDto.getEventId())
                .setAvailableSeats(seatBookingUpdatedDto.getAvailableSeats())
                .setTotalSeats(seatBookingUpdatedDto.getTotalSeats())
                .setEventCategory(String.valueOf(seatBookingUpdatedDto.getEventCategory()))
                .build();

        byte[] bytes = seatBookingUpdated.toByteArray();

        try{
            kafkaTemplate.send("seat.booking.updated.v1", bytes);
            log.info("Sent SeatBookingUpdated event");
        }catch(Exception e){
            log.error("Error in sending SeatBookingUpdated event : {}",e.getMessage());
        }
    }
}
