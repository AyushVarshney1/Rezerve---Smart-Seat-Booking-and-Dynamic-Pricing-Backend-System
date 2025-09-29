package com.rezerve.rezerveeventservice.service;

import com.rezerve.rezerveeventservice.exception.EventNotFoundException;
import com.rezerve.rezerveeventservice.model.Event;
import com.rezerve.rezerveeventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCacheableService {

    private final EventRepository eventRepository;

    @Cacheable(value = "events", key = "#eventId")
    public Event getEventEntityById(Long eventId) {
        System.out.println("Fetching event from DB...");
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + " not found"));
    }

}
