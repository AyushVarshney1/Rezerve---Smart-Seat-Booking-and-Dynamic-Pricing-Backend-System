package com.rezerve.rezervenotificationservice.service;

import auth.events.UserCreatedEvent;
import com.rezerve.rezervenotificationservice.model.Notification;
import com.rezerve.rezervenotificationservice.model.enums.NotificationStatus;
import com.rezerve.rezervenotificationservice.model.enums.NotificationType;
import com.rezerve.rezervenotificationservice.repository.NotificationRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import payment.events.PaymentFailedNotificationKafkaEvent;
import payment.events.PaymentSuccessfulNotificationKafkaEvent;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Async
    public void sendUserCreatedNotification(UserCreatedEvent userCreatedEvent){
        Notification notification = new Notification();
        notification.setEmail(userCreatedEvent.getEmail());
        notification.setStatus(NotificationStatus.PENDING);
        notification.setType(NotificationType.USER_CREATED);

        notificationRepository.save(notification);

        try {
            Context context = new Context();
            context.setVariable("userEmail", notification.getEmail());
            context.setVariable("userName", userCreatedEvent.getFullName());
            context.setVariable("phoneNumber", userCreatedEvent.getPhoneNumber());
            String htmlContent = springTemplateEngine.process("user-signup-email", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(notification.getEmail());
            helper.setSubject("🎉 Welcome to Rezerve!");
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);

            notification.setStatus(NotificationStatus.SENT);
            log.info("User Created Notification has been sent");
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            log.error(e.getMessage());
        }

        notificationRepository.save(notification);

    }

    @Async
    public void sendPaymentSuccessfulNotification(PaymentSuccessfulNotificationKafkaEvent paymentSuccessfulNotificationKafkaEvent){
        Notification notification = new Notification();
        notification.setEmail(paymentSuccessfulNotificationKafkaEvent.getEmail());
        notification.setStatus(NotificationStatus.PENDING);
        notification.setType(NotificationType.PAYMENT_SUCCESSFUL);

        notificationRepository.save(notification);

        try {
            Context context = new Context();
            context.setVariable("bookingId", paymentSuccessfulNotificationKafkaEvent.getBookingId());
            context.setVariable("paymentId", paymentSuccessfulNotificationKafkaEvent.getPaymentId());
            context.setVariable("amount", paymentSuccessfulNotificationKafkaEvent.getAmount());
            String htmlContent = springTemplateEngine.process("payment-successful-email", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(notification.getEmail());
            helper.setSubject("Payment Successful!");
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);

            notification.setStatus(NotificationStatus.SENT);
            log.info("Payment Successful Notification has been sent");
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            log.error(e.getMessage());
        }

        notificationRepository.save(notification);
    }

    @Async
    public void sendPaymentFailedNotification(PaymentFailedNotificationKafkaEvent paymentFailedNotificationKafkaEvent){
        Notification notification = new Notification();
        notification.setEmail(paymentFailedNotificationKafkaEvent.getEmail());
        notification.setStatus(NotificationStatus.PENDING);
        notification.setType(NotificationType.PAYMENT_FAILED);

        notificationRepository.save(notification);

        try {
            Context context = new Context();
            context.setVariable("bookingId", paymentFailedNotificationKafkaEvent.getBookingId());
            context.setVariable("paymentId", paymentFailedNotificationKafkaEvent.getPaymentId());
            context.setVariable("amount", paymentFailedNotificationKafkaEvent.getAmount());
            context.setVariable("failureReason", paymentFailedNotificationKafkaEvent.getMessage());
            String htmlContent = springTemplateEngine.process("payment-failed-email", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(notification.getEmail());
            helper.setSubject("Payment Failed!");
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);

            notification.setStatus(NotificationStatus.SENT);
            log.info("Payment Failed Notification has been sent");
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            log.error(e.getMessage());
        }

        notificationRepository.save(notification);
    }
}
