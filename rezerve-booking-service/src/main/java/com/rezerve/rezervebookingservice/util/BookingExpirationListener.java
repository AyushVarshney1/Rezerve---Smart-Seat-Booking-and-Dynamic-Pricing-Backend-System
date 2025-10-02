package com.rezerve.rezervebookingservice.util;

import com.rezerve.rezervebookingservice.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookingExpirationListener extends KeyExpirationEventMessageListener {

    private final BookingService bookingService;
    private static final String BOOKING_CACHE_KEY_PREFIX = "booking:";
    private static final Logger log = LoggerFactory.getLogger(BookingExpirationListener.class);

    public BookingExpirationListener(
            RedisMessageListenerContainer listenerContainer,
            BookingService bookingService) {
        super(listenerContainer);
        this.bookingService = bookingService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("Redis key expired: {}", expiredKey);

        if (expiredKey.startsWith(BOOKING_CACHE_KEY_PREFIX)) {
            String bookingIdStr = expiredKey.substring(BOOKING_CACHE_KEY_PREFIX.length());

            try {
                UUID bookingId = UUID.fromString(bookingIdStr);
                bookingService.handleBookingExpiration(bookingId);
            } catch (IllegalArgumentException e) {
                log.error("Invalid booking ID format: {}", bookingIdStr, e);
            }
        }
    }
}
