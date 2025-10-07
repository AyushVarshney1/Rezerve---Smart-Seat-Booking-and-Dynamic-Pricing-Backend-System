package com.rezerve.rezervepricingservice.kafka;

import event.events.EventPriceUpdatedKafkaEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPriceKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(EventPriceKafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEventPriceUpdatedEvent(EventPriceUpdatedKafkaEvent eventPriceUpdatedKafkaEvent){
        byte[] bytes = eventPriceUpdatedKafkaEvent.toByteArray();

        try{
            kafkaTemplate.send("event.price.updated.topic.v1", bytes);
            log.info("Event Price Updated Event Successfully to topic");
        }catch (Exception e){
            log.error("Error in sending EventPriceUpdatedEvent ",e);
        }
    }


}
