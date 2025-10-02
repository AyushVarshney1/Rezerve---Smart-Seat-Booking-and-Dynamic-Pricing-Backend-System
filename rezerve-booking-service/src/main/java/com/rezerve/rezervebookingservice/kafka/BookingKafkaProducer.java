package com.rezerve.rezervebookingservice.kafka;

import booking.events.ReleaseSeatsEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(BookingKafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendReleaseSeatEvent(Long eventId, Integer totalSeats){
        ReleaseSeatsEvent releaseSeatsEvent = ReleaseSeatsEvent.newBuilder()
                .setEventId(eventId)
                .setSeats(totalSeats)
                .build();

        byte[] bytes = releaseSeatsEvent.toByteArray();

        try{
            kafkaTemplate.send("release.seat.topic.v1",bytes);
            log.info("Sent release seat event to topic");
        }catch (Exception e){
            log.error("Error in ReleaseSeatsEvent sending releaseSeatsEvent: {} ", e.getMessage());
        }
    }
}
