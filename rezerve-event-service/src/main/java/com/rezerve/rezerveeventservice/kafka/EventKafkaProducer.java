package com.rezerve.rezerveeventservice.kafka;

import com.rezerve.rezerveeventservice.dto.request.EventPriceProducerDto;
import com.rezerve.rezerveeventservice.dto.request.EventProducerDto;
import event.events.EventCreatedKafkaEvent;
import event.events.EventDeletedKafkaEvent;
import event.events.EventPriceKafkaEvent;
import event.events.EventSeatsUpdatedKafkaEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(EventKafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEventCreatedKafkaEvent(EventProducerDto eventProducerDto){
        EventCreatedKafkaEvent eventCreatedKafkaEvent = EventCreatedKafkaEvent.newBuilder()
                .setEventId(eventProducerDto.getEventId())
                .setTotalSeats(eventProducerDto.getTotalSeats())
                .setEventType(String.valueOf(eventProducerDto.getEventCategory()))
                .build();

        byte[] bytes = eventCreatedKafkaEvent.toByteArray();

        try{
            kafkaTemplate.send("event.created.topic.v1",bytes);
            log.info("Sent event created to topic");
        }catch (Exception e){
            log.error("Error in EventCreatedKafkaEvent sending eventCreatedKafkaEvent");
        }
    }

    public void sendEventSeatsUpdatedKafkaEvent(EventProducerDto eventProducerDto){
        EventSeatsUpdatedKafkaEvent eventSeatsUpdatedKafkaEvent = EventSeatsUpdatedKafkaEvent.newBuilder()
                .setEventId(eventProducerDto.getEventId())
                .setTotalSeats(eventProducerDto.getTotalSeats())
                .build();

        byte[] bytes = eventSeatsUpdatedKafkaEvent.toByteArray();

        try{
            kafkaTemplate.send("event.seats.updated.topic.v1",bytes);
            log.info("Sent event seats updated to topic");
        }catch (Exception e){
            log.error("Error in EventSeatsUpdatedKafkaEvent sending eventSeatsUpdatedKafkaEvent");
        }
    }

    public void sendEventDeletedKafkaEvent(EventProducerDto eventProducerDto){
        EventDeletedKafkaEvent eventDeletedKafkaEvent = EventDeletedKafkaEvent.newBuilder()
                .setEventId(eventProducerDto.getEventId())
                .build();

        byte[] bytes = eventDeletedKafkaEvent.toByteArray();

        try{
            kafkaTemplate.send("event.deleted.topic.v1",bytes);
            log.info("Sent event deleted to topic");
        }catch (Exception e){
            log.error("Error in EventDeletedKafkaEvent sending eventDeletedKafkaEvent");
        }
    }

    public void sendEventPriceKafkaEvent(EventPriceProducerDto eventPriceProducerDto){
        EventPriceKafkaEvent eventPriceKafkaEvent = EventPriceKafkaEvent.newBuilder()
                .setEventId(eventPriceProducerDto.getEventId())
                .setBasePrice(eventPriceProducerDto.getPrice())
                .setEventCategory(String.valueOf(eventPriceProducerDto.getEventCategory()))
                .build();

        byte[] bytes = eventPriceKafkaEvent.toByteArray();

        try{
            kafkaTemplate.send("event.price.topic.v1",bytes);
            log.info("Sent event price event to topic");
        }catch (Exception e){
            log.error("Error in EventPriceKafkaEvent sending eventPriceKafkaEvent : {}", e.getMessage());
        }
    }
}
