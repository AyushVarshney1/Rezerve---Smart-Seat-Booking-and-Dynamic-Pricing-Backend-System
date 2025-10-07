package com.rezerve.rezervenotificationservice.kafka;

import auth.events.UserCreatedEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import com.rezerve.rezervenotificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationKafkaConsumer.class);
    private final NotificationService notificationService;

    @KafkaListener(topics = "user.created.topic.v1", groupId = "rezerve-notification-serivce")
    public void consumeUserCreatedEvent(byte[] bytes){
        try{
            UserCreatedEvent userCreatedEvent = UserCreatedEvent.parseFrom(bytes);
        }catch(InvalidProtocolBufferException e){
            log.error("Error while parsing UserCreatedEvent", e);
        }
    }
}
