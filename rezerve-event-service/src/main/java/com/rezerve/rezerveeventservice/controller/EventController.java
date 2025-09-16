package com.rezerve.rezerveeventservice.controller;

import com.rezerve.rezerveeventservice.dto.request.EventUpdateRequestDto;
import com.rezerve.rezerveeventservice.dto.request.TravelEventRequestDto;
import com.rezerve.rezerveeventservice.dto.request.VenueEventRequestDto;
import com.rezerve.rezerveeventservice.dto.response.EventResponseDto;
import com.rezerve.rezerveeventservice.exception.InvalidRequestBodyException;
import com.rezerve.rezerveeventservice.exception.InvalidHeaderException;
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

    // CREATE A NEW TRAVEL EVENT (ONLY ADMINS CAN CREATE A TRAVEL EVENT)
    @PostMapping("/create-travel-event")
    public ResponseEntity<EventResponseDto> createTravelEvent(@RequestHeader("Authorization") String header, @Valid @RequestBody TravelEventRequestDto travelEventRequestDto){
        if(header == null || !header.startsWith("Bearer ")) {
            throw new InvalidHeaderException("Invalid Header");
        }

        String token = header.substring(7);

        if(!travelEventRequestDto.getCategory().equals(EventCategory.FLIGHT) && !travelEventRequestDto.getCategory().equals(EventCategory.BUS) && !travelEventRequestDto.getCategory().equals(EventCategory.TRAIN)){
            throw new InvalidRequestBodyException("Invalid travel category event");
        }

        EventResponseDto eventResponseDto = eventService.createEvent(token, travelEventRequestDto);

        return ResponseEntity.ok(eventResponseDto);
    }

    // CREATE A NEW VENUE EVENT (ONLY ADMINS CAN CREATE A VENUE EVENT)
    @PostMapping("/create-venue-event")
    public ResponseEntity<EventResponseDto> createVenueEvent(@RequestHeader("Authorization") String header, @Valid @RequestBody VenueEventRequestDto venueEventRequestDto){
        if(header == null || !header.startsWith("Bearer ")) {
            throw new InvalidHeaderException("Invalid Header");
        }

        String token = header.substring(7);

        if(!venueEventRequestDto.getCategory().equals(EventCategory.MOVIE) && !venueEventRequestDto.getCategory().equals(EventCategory.CONCERT)){
            throw new InvalidRequestBodyException("Invalid venue category event");
        }

        EventResponseDto eventResponseDto = eventService.createEvent(token, venueEventRequestDto);

        return ResponseEntity.ok(eventResponseDto);
    }

    // UPDATE EVENT (WE DON'T ALLOW LOCATION CHANGES OF AN EVENT) (ONLY ADMINS CAN UPDATE AN EVENT)
    @PatchMapping("/update-event")
    public ResponseEntity<EventResponseDto> updateEvent(@RequestHeader("Authorization") String header, @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto){
        if(header == null || !header.startsWith("Bearer ")) {
            throw new InvalidHeaderException("Invalid Header");
        }

        if(eventUpdateRequestDto.getDescription() == null && eventUpdateRequestDto.getPrice()== null && eventUpdateRequestDto.getName() == null && eventUpdateRequestDto.getStartTime() == null && eventUpdateRequestDto.getEndTime() == null && eventUpdateRequestDto.getTotalSeats() == null){
            throw new InvalidRequestBodyException("Invalid request body. At least one field is required");
        }

        String token = header.substring(7);

        EventResponseDto eventResponseDto = eventService.updateEvent(token,eventUpdateRequestDto);

        return ResponseEntity.ok(eventResponseDto);
    }

    // DELETE EVENT (ONLY ADMINS CAN DELETE AN EVENT)
    @DeleteMapping("/delete-event/{eventId}")
    public ResponseEntity<EventResponseDto> deleteEvent(@RequestHeader("Authorization") String header, @PathVariable("eventId") Long eventId){
        if(header == null || !header.startsWith("Bearer ")) {
            throw new InvalidHeaderException("Invalid Header");
        }

        String token = header.substring(7);

        eventService.deleteEvent(token,eventId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
