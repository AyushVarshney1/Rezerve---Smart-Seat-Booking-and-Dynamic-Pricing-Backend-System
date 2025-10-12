package com.rezerve.rezervebookingservice.util;

import com.rezerve.rezervebookingservice.model.Booking;
import com.rezerve.rezervebookingservice.model.enums.BookingStatus;
import com.rezerve.rezervebookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingCancellationScheduler {

    private static final Logger log = LoggerFactory.getLogger(BookingCancellationScheduler.class);
    private final BookingRepository bookingRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelBookings() {
        LocalDateTime tenMinAgo = LocalDateTime.now().minusMinutes(10);

        log.info("Fetching expired bookings from database.....");
        List<Booking> expiredBookings = bookingRepository.findByCreatedDateLessThanAndBookingStatus(tenMinAgo, BookingStatus.PENDING);

        log.info("List of expired bookings: {}", expiredBookings);

        if(!expiredBookings.isEmpty()){
            expiredBookings.forEach(booking -> booking.setBookingStatus(BookingStatus.CANCELLED));
            bookingRepository.saveAll(expiredBookings);
            log.info("Marked expired bookings as CANCELLED");
        }
    }
}
