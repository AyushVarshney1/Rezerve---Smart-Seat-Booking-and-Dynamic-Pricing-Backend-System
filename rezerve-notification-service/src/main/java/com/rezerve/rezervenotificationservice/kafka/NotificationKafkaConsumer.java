package com.rezerve.rezervenotificationservice.kafka;

import auth.events.UserCreatedEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import com.rezerve.rezervenotificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import payment.events.PaymentFailedNotificationKafkaEvent;
import payment.events.PaymentSuccessfulNotificationKafkaEvent;

@Service
@RequiredArgsConstructor
public class NotificationKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationKafkaConsumer.class);
    private final NotificationService notificationService;

    @KafkaListener(topics = "user.created.topic.v1", groupId = "rezerve-notification-service")
    public void consumeUserCreatedEvent(byte[] bytes){
        try{
            UserCreatedEvent userCreatedEvent = UserCreatedEvent.parseFrom(bytes);
        }catch(InvalidProtocolBufferException e){
            log.error("Error while parsing UserCreatedEvent", e);
        }
    }

    @KafkaListener(topics = "payment.successful.notification.topic.v1", groupId = "rezerve-notification-service")
    public void consumePaymentSuccessfulNotificationEvent(byte[] bytes){
        try{
            PaymentSuccessfulNotificationKafkaEvent paymentSuccessfulNotificationKafkaEvent = PaymentSuccessfulNotificationKafkaEvent.parseFrom(bytes);
        }catch(InvalidProtocolBufferException e){
            log.error("Error while parsing PaymentSuccessfulNotificationKafkaEvent", e);
        }
    }

    @KafkaListener(topics = "payment.failed.notification.topic.v1", groupId = "rezerve-notification-service")
    public void consumePaymentFailedNotificationEvent(byte[] bytes){
        try{
            PaymentFailedNotificationKafkaEvent paymentFailedNotificationKafkaEvent = PaymentFailedNotificationKafkaEvent.parseFrom(bytes);
        }catch(InvalidProtocolBufferException e){
            log.error("Error while parsing PaymentFailedNotificationKafkaEvent", e);
        }
    }
}
