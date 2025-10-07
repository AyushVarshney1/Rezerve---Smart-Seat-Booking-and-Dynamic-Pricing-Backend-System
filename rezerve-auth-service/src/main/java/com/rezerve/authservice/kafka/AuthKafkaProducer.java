package com.rezerve.authservice.kafka;

import auth.events.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(AuthKafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendUserCreatedEvent(String email, String fullName, String phoneNumber){
        UserCreatedEvent userCreatedEvent = UserCreatedEvent.newBuilder()
                .setEmail(email)
                .setFullName(fullName)
                .setPhoneNumber(phoneNumber)
                .build();

        byte[] bytes = userCreatedEvent.toByteArray();

        try{
            kafkaTemplate.send("user.created.topic.v1", bytes);
            log.info("Sent Kafka event for user created to topic: {}", userCreatedEvent);
        }catch(Exception e){
            log.error("Error sending UserCreatedEvent", e);
        }
    }
}
