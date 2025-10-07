package com.rezerve.rezerveeventservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.rezerve.rezerveeventservice.service.EventService;
import event.events.EventPriceUpdatedKafkaEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventKafkaConsumer {

    private static final Logger log =  LoggerFactory.getLogger(EventKafkaConsumer.class);
    private final EventService eventService;

    @KafkaListener(topics = "event.price.updated.topic.v1", groupId = "rezerve-event-service")
    public void handleEventPriceUpdatedEvent(byte[] bytes){
        try{
            EventPriceUpdatedKafkaEvent eventPriceUpdatedKafkaEvent = EventPriceUpdatedKafkaEvent.parseFrom(bytes);

            eventService.updateEventPrice(eventPriceUpdatedKafkaEvent.getEventId(), eventPriceUpdatedKafkaEvent.getNewPrice());
        }catch(InvalidProtocolBufferException e){
            log.error("Error deserializing event price updated event {}", e.getMessage());
        }
    }
}
