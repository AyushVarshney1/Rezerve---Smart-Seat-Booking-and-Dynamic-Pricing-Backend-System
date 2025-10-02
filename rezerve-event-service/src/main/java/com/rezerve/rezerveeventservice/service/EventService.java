package com.rezerve.rezerveeventservice.service;

import com.rezerve.rezerveeventservice.dto.request.EventUpdateRequestDto;
import com.rezerve.rezerveeventservice.dto.request.TravelEventRequestDto;
import com.rezerve.rezerveeventservice.dto.request.VenueEventRequestDto;
import com.rezerve.rezerveeventservice.dto.response.AuthServiceGrpcResponseDto;
import com.rezerve.rezerveeventservice.dto.response.EventResponseDto;
import com.rezerve.rezerveeventservice.dto.response.EventServiceGrpcResponseDto;
import com.rezerve.rezerveeventservice.exception.EventNotFoundException;
import com.rezerve.rezerveeventservice.exception.UnauthorisedException;
import com.rezerve.rezerveeventservice.grpc.AuthServiceGrpcClient;
import com.rezerve.rezerveeventservice.kafka.EventKafkaProducer;
import com.rezerve.rezerveeventservice.mapper.EventMapper;
import com.rezerve.rezerveeventservice.model.Event;
import com.rezerve.rezerveeventservice.repository.EventRepository;
import com.rezerve.rezerveeventservice.util.CheckUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
    private final EventKafkaProducer eventKafkaProducer;
    private final EventCacheableService eventCacheableService;
    private final CacheManager cacheManager;

    public List<EventResponseDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();

        return events.stream().map(eventMapper::toEventResponseDto).collect(Collectors.toList());
    }

    public EventResponseDto getEventById(Long eventId) {
        Event event = eventCacheableService.getEventEntityById(eventId);

        return eventMapper.toEventResponseDto(event);
    }

    public EventResponseDto createTravelEvent(String token, TravelEventRequestDto travelEventRequestDto) {
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);
        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only admins can create event");
        }

        Event event = eventMapper.toTravelEvent(travelEventRequestDto);
        eventRepository.save(event);

        cacheManager.getCache("events").put(event.getId(), event);

        eventKafkaProducer.sendEventCreatedKafkaEvent(eventMapper.toEventProducerDto(event.getId(),event.getTotalSeats(),event.getCategory()));

        return  eventMapper.toEventResponseDto(event);
    }

    public EventResponseDto createVenueEvent(String token, VenueEventRequestDto venueEventRequestDto) {
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);
        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only admins can create event");
        }

        Event event = eventMapper.toVenueEvent(venueEventRequestDto);
        eventRepository.save(event);

        eventKafkaProducer.sendEventCreatedKafkaEvent(eventMapper.toEventProducerDto(event.getId(),event.getTotalSeats(),event.getCategory()));

        return  eventMapper.toEventResponseDto(event);
    }

    @CachePut(value = "events", key = "#eventId")
    public EventResponseDto updateEvent(String token, Long eventId, EventUpdateRequestDto eventUpdateRequestDto) {
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);
        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only admins can update event");
        }

        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + " not found"));

        Event newEvent = checkUpdateRequest.isUpdateRequestValid(oldEvent, eventUpdateRequestDto);
        eventRepository.save(newEvent);

        if(eventUpdateRequestDto.getTotalSeats() != null){
            eventKafkaProducer.sendEventSeatsUpdatedKafkaEvent(
                    eventMapper.toEventProducerDto(newEvent.getId(), newEvent.getTotalSeats(), null)
            );
        }

        return eventMapper.toEventResponseDto(newEvent);
    }

    @CacheEvict(value = "events", key = "#eventId")
    public void deleteEvent(String token, Long eventId) {
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);
        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only admins can delete event");
        }

        if(eventRepository.findById(eventId).isEmpty()){
            throw new EventNotFoundException("Event with id: " + eventId + " not found");
        }

        eventRepository.deleteById(eventId);
        eventKafkaProducer.sendEventDeletedKafkaEvent(
                eventMapper.toEventProducerDto(eventId, null, null)
        );
    }

    public EventServiceGrpcResponseDto getEventDetailsForBooking(Long eventId) {
        try {
            Event event = eventCacheableService.getEventEntityById(eventId);
            return eventMapper.toSuccessEventServiceGrpcResponseDto(event);
        } catch (EventNotFoundException e) {
            return eventMapper.toFailedEventServiceGrpcResponseDto();
        }
    }

}
