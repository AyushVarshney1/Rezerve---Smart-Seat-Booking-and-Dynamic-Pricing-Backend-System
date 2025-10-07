package com.rezerve.rezerveeventservice.service;

import com.rezerve.rezerveeventservice.dto.response.EventResponseDto;
import com.rezerve.rezerveeventservice.exception.EventNotFoundException;
import com.rezerve.rezerveeventservice.mapper.EventMapper;
import com.rezerve.rezerveeventservice.model.Event;
import com.rezerve.rezerveeventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCacheableService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Cacheable(value = "events", key = "#eventId")
    public EventResponseDto getEventEntityById(Long eventId) {
        System.out.println("Fetching event from DB...");
        Event event =  eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + " not found"));

        return eventMapper.toEventResponseDto(event);
    }

}
