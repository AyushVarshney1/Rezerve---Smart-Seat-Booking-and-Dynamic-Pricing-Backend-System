package com.rezerve.rezervepricingservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.rezerve.rezervepricingservice.mapper.PricingMapper;
import com.rezerve.rezervepricingservice.service.PricingService;
import event.events.EventPriceKafkaEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPriceKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventPriceKafkaConsumer.class);
    private final PricingService pricingService;
    private final PricingMapper  pricingMapper;

    @KafkaListener(topics = "event.price.topic.v1", groupId = "rezerve-pricing-service")
    public void consumePaymentSuccessfulEvent(byte[] bytes){
        try{
            EventPriceKafkaEvent eventPriceKafkaEvent = EventPriceKafkaEvent.parseFrom(bytes);
            log.info("Received Event Price event {}", eventPriceKafkaEvent);

            pricingService.createEventPrice(pricingMapper.toEventPrice(eventPriceKafkaEvent));
        }catch(InvalidProtocolBufferException e){
            log.error("Error deserializing event price event {}", e.getMessage());
        }
    }

}
