package com.rezerve.rezerveeventservice.controller;

import com.rezerve.rezerveeventservice.dto.response.EventResponseDto;
import com.rezerve.rezerveeventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
