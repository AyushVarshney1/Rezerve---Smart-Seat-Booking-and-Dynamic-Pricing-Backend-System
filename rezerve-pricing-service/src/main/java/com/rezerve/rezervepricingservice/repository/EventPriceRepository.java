package com.rezerve.rezervepricingservice.repository;

import com.rezerve.rezervepricingservice.model.EventPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventPriceRepository extends JpaRepository<EventPrice,Long> {

    Optional<EventPrice> findByEventId(Long eventId);
}
