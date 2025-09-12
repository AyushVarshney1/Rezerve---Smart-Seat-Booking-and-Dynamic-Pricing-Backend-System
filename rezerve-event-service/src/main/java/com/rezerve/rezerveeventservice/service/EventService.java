package com.rezerve.rezerveeventservice.service;

import com.rezerve.rezerveeventservice.dto.response.EventResponseDto;
import com.rezerve.rezerveeventservice.mapper.EventMapper;
import com.rezerve.rezerveeventservice.model.Event;
import com.rezerve.rezerveeventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public List<EventResponseDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();

        return events.stream().map(eventMapper::toEventResponseDto).collect(Collectors.toList());
    }
}
