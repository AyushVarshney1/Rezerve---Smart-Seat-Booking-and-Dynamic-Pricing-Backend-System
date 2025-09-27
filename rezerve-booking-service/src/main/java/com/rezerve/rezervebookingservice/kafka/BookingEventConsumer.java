package com.rezerve.rezervebookingservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.rezerve.rezervebookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import payment.events.PaymentSuccessfulKafkaEvent;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(BookingEventConsumer.class);
    private final BookingService bookingService;

    @KafkaListener(topics = "payment.successful.topic.v1", groupId = "rezerve-booking-service")
    public void consumePaymentSuccessfulEvent(byte[] bytes){
        try{
            PaymentSuccessfulKafkaEvent paymentSuccessfulKafkaEvent = PaymentSuccessfulKafkaEvent.parseFrom(bytes);
            log.info("Received Payment Successful event {}", paymentSuccessfulKafkaEvent);

            bookingService.completeBooking(UUID.fromString(paymentSuccessfulKafkaEvent.getBookingId()));
        }catch(InvalidProtocolBufferException e){
            log.error("Error deserializing event created event {}", e.getMessage());
        }
    }
}
