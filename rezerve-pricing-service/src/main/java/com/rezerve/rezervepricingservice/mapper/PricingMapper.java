package com.rezerve.rezervepricingservice.mapper;

import com.rezerve.rezervepricingservice.model.EventPrice;
import com.rezerve.rezervepricingservice.model.enums.EventCategory;
import event.events.EventPriceKafkaEvent;
import org.springframework.stereotype.Component;

@Component
public class PricingMapper {

    public EventPrice toEventPrice(EventPriceKafkaEvent eventPriceKafkaEvent) {
        EventPrice eventPrice = new EventPrice();

        eventPrice.setEventId(eventPriceKafkaEvent.getEventId());
        eventPrice.setBasePrice(eventPriceKafkaEvent.getBasePrice());
        eventPrice.setCurrentPrice(eventPriceKafkaEvent.getBasePrice());
        eventPrice.setEventCategory(EventCategory.valueOf(eventPriceKafkaEvent.getEventCategory()));

        return eventPrice;
    }
}
