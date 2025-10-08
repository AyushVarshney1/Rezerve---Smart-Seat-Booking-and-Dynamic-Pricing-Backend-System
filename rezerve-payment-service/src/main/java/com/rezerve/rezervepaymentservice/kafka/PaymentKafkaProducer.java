package com.rezerve.rezervepaymentservice.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import payment.events.PaymentFailedNotificationKafkaEvent;
import payment.events.PaymentSuccessfulKafkaEvent;
import payment.events.PaymentSuccessfulNotificationKafkaEvent;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(PaymentKafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendPaymentSuccessfulKafkaEvent(UUID bookingId){
        PaymentSuccessfulKafkaEvent paymentSuccessfulKafkaEvent = PaymentSuccessfulKafkaEvent.newBuilder()
                .setBookingId(String.valueOf(bookingId))
                .build();

        byte[] bytes = paymentSuccessfulKafkaEvent.toByteArray();

        try {
            kafkaTemplate.send("payment.successful.topic.v1",bytes);
            log.info("Sent payment successful to topic");
        }catch (Exception e){
            log.error("Error in PaymentSuccessfulKafkaEvent sending sendPaymentSuccessfulKafkaEvent: {}", e.getMessage());
        }
    }

    public void sendPaymentSuccessfulNotificationKafkaEvent(UUID bookingId, UUID paymentId, Double amount){
        PaymentSuccessfulNotificationKafkaEvent paymentSuccessfulNotificationKafkaEvent = PaymentSuccessfulNotificationKafkaEvent.newBuilder()
                .setBookingId(String.valueOf(bookingId))
                .setPaymentId(String.valueOf(paymentId))
                .setAmount(amount)
                .build();

        byte[] bytes = paymentSuccessfulNotificationKafkaEvent.toByteArray();

        try {
            kafkaTemplate.send("payment.successful.notification.topic.v1",bytes);
            log.info("Sent payment successful notification to topic");
        }catch (Exception e){
            log.error("Error in PaymentSuccessfulNotificationKafkaEvent sending paymentSuccessfulNotificationKafkaEvent: {}", e.getMessage());
        }
    }

    public void sendPaymentFailedNotificationKafkaEvent(UUID bookingId, UUID paymentId, Double amount, String message){
        PaymentFailedNotificationKafkaEvent paymentFailedNotificationKafkaEvent = PaymentFailedNotificationKafkaEvent.newBuilder()
                .setBookingId(String.valueOf(bookingId))
                .setPaymentId(String.valueOf(paymentId))
                .setAmount(amount)
                .setMessage(message)
                .build();

        byte[] bytes = paymentFailedNotificationKafkaEvent.toByteArray();

        try {
            kafkaTemplate.send("payment.failed.notification.topic.v1",bytes);
            log.info("Sent payment failed notification to topic");
        }catch (Exception e){
            log.error("Error in PaymentFailedNotificationKafkaEvent sending paymentFailedNotificationKafkaEvent: {}", e.getMessage());
        }
    }


}
