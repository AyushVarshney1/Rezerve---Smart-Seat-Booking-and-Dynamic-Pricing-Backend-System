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
        log.info("Received UserCreatedEvent from Kafka");
        try{
            UserCreatedEvent userCreatedEvent = UserCreatedEvent.parseFrom(bytes);
            notificationService.sendUserCreatedNotification(userCreatedEvent);
        }catch(InvalidProtocolBufferException e){
            log.error("Error while parsing UserCreatedEvent", e);
        }
    }

    @KafkaListener(topics = "payment.successful.notification.topic.v1", groupId = "rezerve-notification-service")
    public void consumePaymentSuccessfulNotificationEvent(byte[] bytes){
        log.info("Received PaymentSuccessfulNotificationKafkaEvent from Kafka");
        try{
            PaymentSuccessfulNotificationKafkaEvent paymentSuccessfulNotificationKafkaEvent = PaymentSuccessfulNotificationKafkaEvent.parseFrom(bytes);

            notificationService.sendPaymentSuccessfulNotification(paymentSuccessfulNotificationKafkaEvent);

        }catch(InvalidProtocolBufferException e){
            log.error("Error while parsing PaymentSuccessfulNotificationKafkaEvent", e);
        }
    }

    @KafkaListener(topics = "payment.failed.notification.topic.v1", groupId = "rezerve-notification-service")
    public void consumePaymentFailedNotificationEvent(byte[] bytes){
        log.info("Received PaymentFailedNotificationKafkaEvent from Kafka");
        try{
            PaymentFailedNotificationKafkaEvent paymentFailedNotificationKafkaEvent = PaymentFailedNotificationKafkaEvent.parseFrom(bytes);
            notificationService.sendPaymentFailedNotification(paymentFailedNotificationKafkaEvent);

        }catch(InvalidProtocolBufferException e){
            log.error("Error while parsing PaymentFailedNotificationKafkaEvent", e);
        }
    }
}
