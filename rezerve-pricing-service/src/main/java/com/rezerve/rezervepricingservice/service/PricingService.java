package com.rezerve.rezervepricingservice.service;

import com.rezerve.rezervepricingservice.kafka.EventPriceKafkaProducer;
import com.rezerve.rezervepricingservice.model.EventPrice;
import com.rezerve.rezervepricingservice.model.enums.EventCategory;
import com.rezerve.rezervepricingservice.repository.EventPriceRepository;
import event.events.EventPriceUpdatedKafkaEvent;
import inventory.events.SeatBookingUpdated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricingService {

    private static final Logger log = LoggerFactory.getLogger(PricingService.class);
    private final EventPriceRepository eventPriceRepository;
    private final EventPriceKafkaProducer eventPriceKafkaProducer;

    public void createEventPrice(EventPrice eventPrice){
        eventPriceRepository.save(eventPrice);
    }

    public void handleDynamicPricing(SeatBookingUpdated seatBookingUpdated){
        Long eventId = seatBookingUpdated.getEventId();
        EventPrice eventPrice = eventPriceRepository.findByEventId(eventId).orElseThrow(() -> new RuntimeException("Event with id: " + eventId + " not found"));
        Double basePrice = eventPrice.getBasePrice();
        int seatsBooked = seatBookingUpdated.getTotalSeats() - seatBookingUpdated.getAvailableSeats();
        int totalSeats = seatBookingUpdated.getTotalSeats();
        EventCategory eventCategory = EventCategory.valueOf(seatBookingUpdated.getEventCategory());

        log.info("EventId : {}, basePrice: {}, seatsBooked: {}, totalSeats: {}, EventCategory: {}", eventId, basePrice, seatsBooked, totalSeats, eventCategory);

        double dynamicPrice = Math.floor(calculatePrice(eventCategory, basePrice, seatsBooked, totalSeats));

        log.info("DynamicPrice : {}", dynamicPrice);

        eventPrice.setCurrentPrice(dynamicPrice);

        // LAZY UPDATING OF TOTAL SEATS BY INVENTORY SERVICE (WHEN EVENT SERVICE UPDATES IT'S TOTALSEATS,
        // IT ALSO UPDATES INVENTORY SERVICE BUT NOT PRICING SERVICE AS IT IS NOT THAT HIGH PRIORITY AND
        // PRICING SERVICE CAN LATER ON HAVE UPDATED TOTAL SEATS BY INVENTORY SERVICE'S KAFKA EVENT)
        eventPrice.setTotalSeats(totalSeats);
        eventPrice.setAvailableSeats(totalSeats - seatsBooked);

        eventPriceRepository.save(eventPrice);

        EventPriceUpdatedKafkaEvent eventPriceUpdatedKafkaEvent = EventPriceUpdatedKafkaEvent.newBuilder()
                .setEventId(eventId)
                .setNewPrice(dynamicPrice)
                .build();

        eventPriceKafkaProducer.sendEventPriceUpdatedEvent(eventPriceUpdatedKafkaEvent);

    }

    public Double calculatePrice(EventCategory eventCategory, Double basePrice, int seatsBooked, int totalSeats){
        if(seatsBooked <= 0){
            return basePrice;
        }

        return switch (eventCategory) {
            case FLIGHT -> calculateFlightPrice(basePrice, seatsBooked, totalSeats);
            case BUS -> calculateBusPrice(basePrice, seatsBooked, totalSeats);
            case TRAIN -> calculateTrainPrice(basePrice, seatsBooked, totalSeats);
            case CONCERT -> calculateConcertPrice(basePrice, seatsBooked, totalSeats);
            default -> basePrice;
        };
    }

    // CONCERT: strong demand-based + scarcity pricing
    private static double calculateConcertPrice(double basePrice, int seatsBooked, int totalSeats) {
        double occupancy = (double) seatsBooked / totalSeats;

        // Smooth demand curve (up to +50%)
        double demandFactor = 1 + (occupancy * 0.5);

        // Scarcity effect (>80%)
        double scarcity = occupancy > 0.8 ? 1.25 : 1.0; // +25% if >80% booked

        // Threshold bonus (sharp jumps)
        double thresholdBonus = 0.0;
        if (occupancy > 0.5) thresholdBonus += basePrice * 0.1; // +10% after 50%
        if (occupancy > 0.8) thresholdBonus += basePrice * 0.2; // +20% after 80%

        return basePrice * demandFactor * scarcity + thresholdBonus;
    }

    // FLIGHT: non-linear airline-style pricing
    private static double calculateFlightPrice(double basePrice, int seatsBooked, int totalSeats) {
        double occupancy = (double) seatsBooked / totalSeats;

        // Logarithmic demand curve (up to ~+70%)
        double demandFactor = 1 + Math.log1p(occupancy * 10) / 5;

        // Scarcity multiplier
        double scarcity = occupancy > 0.9 ? 1.4 : occupancy > 0.75 ? 1.2 : 1.0;

        return basePrice * demandFactor * scarcity;
    }

    // BUS: mild dynamic pricing
    private static double calculateBusPrice(double basePrice, int seatsBooked, int totalSeats) {
        double occupancy = (double) seatsBooked / totalSeats;

        // Mild demand scaling (up to +25%)
        double demandFactor = 1 + (occupancy * 0.25);

        // Small scarcity effect
        double scarcity = occupancy > 0.85 ? 1.1 : 1.0; // +10% if >85% booked

        return basePrice * demandFactor * scarcity;
    }

    // TRAIN: mostly fixed, small occupancy-based rise
    private static double calculateTrainPrice(double basePrice, int seatsBooked, int totalSeats) {
        double occupancy = (double) seatsBooked / totalSeats;

        // Small linear increase (up to +15%)
        double demandFactor = 1 + (occupancy * 0.15);

        // Additional minor scarcity for high occupancy
        double scarcity = occupancy > 0.9 ? 1.1 : 1.0;

        return basePrice * demandFactor * scarcity;
    }

}
