package com.rezerve.rezerveeventservice.service;

import com.rezerve.rezerveeventservice.dto.request.EventUpdateRequestDto;
import com.rezerve.rezerveeventservice.dto.request.TravelEventRequestDto;
import com.rezerve.rezerveeventservice.dto.request.VenueEventRequestDto;
import com.rezerve.rezerveeventservice.dto.response.AuthServiceGrpcResponseDto;
import com.rezerve.rezerveeventservice.dto.response.EventResponseDto;
import com.rezerve.rezerveeventservice.exception.EventNotFoundException;
import com.rezerve.rezerveeventservice.exception.UnauthorisedException;
import com.rezerve.rezerveeventservice.grpc.AuthServiceGrpcClient;
import com.rezerve.rezerveeventservice.mapper.EventMapper;
import com.rezerve.rezerveeventservice.model.Event;
import com.rezerve.rezerveeventservice.repository.EventRepository;
import com.rezerve.rezerveeventservice.util.CheckUpdateRequest;
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
    private final CheckUpdateRequest checkUpdateRequest;

    public List<EventResponseDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();

        return events.stream().map(eventMapper::toEventResponseDto).collect(Collectors.toList());
    }

    public EventResponseDto getEventById(String eventId) {
        Event event = eventRepository.findById(Long.parseLong(eventId)).orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + " not found"));

        return eventMapper.toEventResponseDto(event);
    }

    public EventResponseDto createTravelEvent(String token, TravelEventRequestDto travelEventRequestDto) {
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);
        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only admins can create event");
        }

        Event event = eventMapper.toTravelEvent(travelEventRequestDto);

        return  eventMapper.toEventResponseDto(eventRepository.save(event));
    }

    public EventResponseDto createVenueEvent(String token, VenueEventRequestDto venueEventRequestDto) {
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);
        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only admins can create event");
        }

        Event event = eventMapper.toVenueEvent(venueEventRequestDto);

        return  eventMapper.toEventResponseDto(eventRepository.save(event));
    }

    public EventResponseDto updateEvent(String token, Long eventId, EventUpdateRequestDto eventUpdateRequestDto) {
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);
        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only admins can update event");
        }

        Event oldEvent = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + " not found"));

        Event newEvent = checkUpdateRequest.isUpdateRequestValid(oldEvent, eventUpdateRequestDto);

        return eventMapper.toEventResponseDto(eventRepository.save(newEvent));
    }

    public void deleteEvent(String token, Long eventId) {
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);
        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only admins can delete event");
        }

        if(eventRepository.findById(eventId).isEmpty()){
            throw new  EventNotFoundException("Event with id: " + eventId + " not found");
        }

        eventRepository.deleteById(eventId);
    }
}
