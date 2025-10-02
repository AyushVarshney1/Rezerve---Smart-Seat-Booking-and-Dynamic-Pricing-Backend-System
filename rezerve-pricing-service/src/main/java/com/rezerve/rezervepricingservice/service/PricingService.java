package com.rezerve.rezervepricingservice.service;

import com.rezerve.rezervepricingservice.model.EventPrice;
import com.rezerve.rezervepricingservice.repository.EventPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final EventPriceRepository eventPriceRepository;

    public void createEventPrice(EventPrice eventPrice){
        eventPriceRepository.save(eventPrice);
    }


}
