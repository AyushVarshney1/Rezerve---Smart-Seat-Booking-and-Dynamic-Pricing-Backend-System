package com.rezerve.rezerveeventservice.controller;

import com.rezerve.rezerveeventservice.dto.request.EventRequestDto;
import com.rezerve.rezerveeventservice.dto.request.TravelEventRequestDto;
import com.rezerve.rezerveeventservice.dto.request.VenueEventRequestDto;
import com.rezerve.rezerveeventservice.dto.response.EventResponseDto;
import com.rezerve.rezerveeventservice.exception.InvalidRequestBodyException;
import com.rezerve.rezerveeventservice.model.enums.EventCategory;
import com.rezerve.rezerveeventservice.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    // RETURN ALL EVENTS
    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        List<EventResponseDto> eventResponseDTOs = eventService.getAllEvents();
        return ResponseEntity.ok(eventResponseDTOs);
    }

    // RETURN SINGLE EVENT BY ID
    @GetMapping("/{event-id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable("event-id") String eventId){
        EventResponseDto eventResponseDto = eventService.getEventById(eventId);
        return ResponseEntity.ok(eventResponseDto);
    }

    // CREATE NEW EVENT TRAVEL EVENT (ONLY ADMIN CAN DO IT)
    @PostMapping("/create-travel-event")
    public ResponseEntity<EventResponseDto> createTravelEvent(@RequestHeader("Authorization") String header, @Valid @RequestBody TravelEventRequestDto travelEventRequestDto){
        if(header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = header.substring(7);

        if(!travelEventRequestDto.getCategory().equals(EventCategory.FLIGHT) && !travelEventRequestDto.getCategory().equals(EventCategory.BUS) && !travelEventRequestDto.getCategory().equals(EventCategory.TRAIN)){
            throw new InvalidRequestBodyException("Invalid travel category event");
        }

        EventResponseDto eventResponseDto = eventService.createEvent(token, travelEventRequestDto);

        return ResponseEntity.ok(eventResponseDto);
    }

    // CREATE NEW EVENT VENUE EVENT (ONLY ADMIN CAN DO IT)
    @PostMapping("/create-venue-event")
    public ResponseEntity<EventResponseDto> createVenueEvent(@RequestHeader("Authorization") String header, @Valid @RequestBody VenueEventRequestDto venueEventRequestDto){
        if(header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = header.substring(7);

        if(!venueEventRequestDto.getCategory().equals(EventCategory.MOVIE) && !venueEventRequestDto.getCategory().equals(EventCategory.CONCERT)){
            throw new InvalidRequestBodyException("Invalid venue category event");
        }

        EventResponseDto eventResponseDto = eventService.createEvent(token, venueEventRequestDto);

        return ResponseEntity.ok(eventResponseDto);
    }
}
