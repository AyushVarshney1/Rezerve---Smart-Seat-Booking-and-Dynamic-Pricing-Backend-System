package com.rezerve.rezerveeventservice.controller;

import com.rezerve.rezerveeventservice.dto.request.EventRequestDto;
import com.rezerve.rezerveeventservice.dto.response.EventResponseDto;
import com.rezerve.rezerveeventservice.service.EventService;
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

    // CREATE NEW EVENT (ONLY ADMIN CAN DO IT)
//    @PostMapping
//    public ResponseEntity<EventResponseDto> createEvent(@RequestHeader("Authorization") String header, @RequestBody EventRequestDto eventRequestDto){
//        if(header == null || !header.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        String token = header.substring(7);
//
//        EventResponseDto eventResponseDto = eventService.createEvent(token, eventRequestDto);
//
//        return ResponseEntity.ok(eventResponseDto);
//    }
}
