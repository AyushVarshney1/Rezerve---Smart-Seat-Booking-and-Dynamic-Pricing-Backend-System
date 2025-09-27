package com.rezerve.rezervepaymentservice.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import payment.events.PaymentSuccessfulKafkaEvent;

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
}
