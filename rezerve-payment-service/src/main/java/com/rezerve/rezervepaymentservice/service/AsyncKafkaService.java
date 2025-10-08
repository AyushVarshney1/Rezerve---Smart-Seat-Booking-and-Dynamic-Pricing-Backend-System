package com.rezerve.rezervepaymentservice.service;

import com.rezerve.rezervepaymentservice.kafka.PaymentKafkaProducer;
import com.rezerve.rezervepaymentservice.model.Payment;
import com.rezerve.rezervepaymentservice.model.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncKafkaService {

    private static final Logger log = LoggerFactory.getLogger(AsyncKafkaService.class);
    private final PaymentKafkaProducer paymentKafkaProducer;

    @Async("KafkaExecutor")
    public void sendAsyncPaymentEvent(Payment payment, String message){

        try{
            if(payment.getPaymentStatus().equals(PaymentStatus.FAILED)){
                paymentKafkaProducer.sendPaymentFailedNotificationKafkaEvent(payment.getBookingId(),payment.getPaymentId(), payment.getAmount(), message);
            }else{
                paymentKafkaProducer.sendPaymentSuccessfulKafkaEvent(payment.getBookingId());
                paymentKafkaProducer.sendPaymentSuccessfulNotificationKafkaEvent(payment.getBookingId(),payment.getPaymentId(), payment.getAmount());
            }
        }catch (Exception e){
            log.error("Exception in AsyncKafkaService sendAsyncPaymentEvent",e);
        }

    }
}
