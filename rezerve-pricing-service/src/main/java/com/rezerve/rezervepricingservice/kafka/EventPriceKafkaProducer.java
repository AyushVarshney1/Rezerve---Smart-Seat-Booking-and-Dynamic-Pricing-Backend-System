package com.rezerve.rezervepricingservice.kafka;

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


}
