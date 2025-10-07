package com.rezerve.authservice.service;

import com.rezerve.authservice.kafka.AuthKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncKafkaService {

    private static final Logger log = LoggerFactory.getLogger(AsyncKafkaService.class);
    private final AuthKafkaProducer authKafkaProducer;

    @Async("KafkaExecutor")
    public void sendAsyncUserCreatedEvent(String email, String fullName, String phoneNumber){
        log.info("Thread: {} - Sending Kafka events", Thread.currentThread().getName());

        try{
            authKafkaProducer.sendUserCreatedEvent(email, fullName, phoneNumber);
        }catch (Exception e){
            log.error("Thread: {} - Sending Kafka event failed", Thread.currentThread().getName());
        }
    }
}
