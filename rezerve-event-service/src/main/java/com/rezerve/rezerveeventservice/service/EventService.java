package com.rezerve.rezerveeventservice.service;

import com.rezerve.rezerveeventservice.dto.request.EventRequestDto;
import com.rezerve.rezerveeventservice.dto.response.AuthServiceGrpcResponseDto;
import com.rezerve.rezerveeventservice.dto.response.EventResponseDto;
import com.rezerve.rezerveeventservice.exception.EventNotFoundException;
import com.rezerve.rezerveeventservice.exception.UnauthorisedException;
import com.rezerve.rezerveeventservice.grpc.AuthServiceGrpcClient;
import com.rezerve.rezerveeventservice.mapper.EventMapper;
import com.rezerve.rezerveeventservice.model.Event;
import com.rezerve.rezerveeventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final AuthServiceGrpcClient authServiceGrpcClient;

    public List<EventResponseDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();

        return events.stream().map(eventMapper::toEventResponseDto).collect(Collectors.toList());
    }

    public EventResponseDto getEventById(String eventId) {
        Event event = eventRepository.findById(Long.parseLong(eventId)).orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + " not found"));

        return eventMapper.toEventResponseDto(event);
    }

    public EventResponseDto createEvent(String token, EventRequestDto eventRequestDto) {
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);
        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only admins can create event");
        }

        Event event = eventMapper.toEvent(eventRequestDto);

        switch(eventRequestDto.getCategory()){
            case BUS, FLIGHT, TRAIN -> {
                event.setFromLocation(eventRequestDto.getFromLocation());
                event.setToLocation(eventRequestDto.getToLocation());
            }
            case MOVIE, CONCERT -> {
                event.setVenueLocation(eventRequestDto.getVenueLocation());
            }
        }

        return  eventMapper.toEventResponseDto(eventRepository.save(event));
    }
}
