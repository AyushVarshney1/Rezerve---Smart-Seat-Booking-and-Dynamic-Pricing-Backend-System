package com.rezerve.rezerveeventservice.service;

import com.rezerve.rezerveeventservice.kafka.EventKafkaProducer;
import com.rezerve.rezerveeventservice.mapper.EventMapper;
import com.rezerve.rezerveeventservice.model.Event;
import com.rezerve.rezerveeventservice.model.enums.EventCategory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncKafkaService {

    private static final Logger log = LoggerFactory.getLogger(AsyncKafkaService.class);
    private final EventMapper eventMapper;
    private final EventKafkaProducer eventKafkaProducer;

    @Async("KafkaExecutor")
    public void sendEventToKafka(Long eventId, Double price, EventCategory eventCategory, Integer totalSeats){
        log.info("Thread: {} - Sending Kafka events", Thread.currentThread().getName());
        try {
            eventKafkaProducer.sendEventCreatedKafkaEvent(
                    eventMapper.toEventProducerDto(
                            eventId,
                            totalSeats,
                            eventCategory
                    )
            );

            eventKafkaProducer.sendEventPriceKafkaEvent(
                    eventMapper.toEventPriceProducerDto(
                            eventId,
                            price,
                            eventCategory,
                            totalSeats
                    )
            );
        } catch (Exception e) {
            log.error("Failed to send Kafka events for event ID: {}", eventId, e);

            // Future Scope:
            // - Store failed events in a DLQ (Dead Letter Queue)
        }
    }

}
