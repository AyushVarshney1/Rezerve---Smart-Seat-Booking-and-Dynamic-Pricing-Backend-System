package com.rezerve.rezerveeventservice.mapper;

import com.rezerve.rezerveeventservice.dto.request.TravelEventRequestDto;
import com.rezerve.rezerveeventservice.dto.request.VenueEventRequestDto;
import com.rezerve.rezerveeventservice.dto.response.*;
import com.rezerve.rezerveeventservice.model.Event;
import org.springframework.stereotype.Component;
import auth.AuthResponse;

@Component
public class EventMapper {

    public EventResponseDto toEventResponseDto(Event event){
        switch (event.getCategory()){
            case FLIGHT,BUS,TRAIN -> {
                TravelEventResponseDto travelEventResponseDto = new TravelEventResponseDto();
                copyBaseEventFields(event, travelEventResponseDto);
                travelEventResponseDto.setFromLocation(event.getFromLocation());
                travelEventResponseDto.setToLocation(event.getToLocation());
                return travelEventResponseDto;
            }
            case MOVIE,CONCERT -> {
                VenueEventResponseDto venueEventResponseDto = new VenueEventResponseDto();
                copyBaseEventFields(event, venueEventResponseDto);
                venueEventResponseDto.setVenueLocation(event.getVenueLocation());
                return venueEventResponseDto;
            }
            default -> {
                EventResponseDto eventResponseDto = new EventResponseDto();
                copyBaseEventFields(event, eventResponseDto);
                return eventResponseDto;
            }
        }
    }

    private void copyBaseEventFields(Event event, EventResponseDto eventResponseDto){
        eventResponseDto.setId(event.getId());
        eventResponseDto.setName(event.getName());
        eventResponseDto.setDescription(event.getDescription());
        eventResponseDto.setPrice(event.getPrice());
        eventResponseDto.setCategory(event.getCategory());
        eventResponseDto.setStartTime(event.getStartTime());
        eventResponseDto.setEndTime(event.getEndTime());
        eventResponseDto.setTotalSeats(event.getTotalSeats());
    }

    public AuthServiceGrpcResponseDto authServiceGrpcResponseDto(AuthResponse authResponse){
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = new AuthServiceGrpcResponseDto();
        authServiceGrpcResponseDto.setUserId(authResponse.getUserId());
        authServiceGrpcResponseDto.setUserEmail(authResponse.getUserEmail());
        authServiceGrpcResponseDto.setUserRole(authResponse.getUserRole());
        return authServiceGrpcResponseDto;
    }

    public Event toTravelEvent(TravelEventRequestDto travelEventRequestDto){
        Event event = new Event();
        event.setCategory(travelEventRequestDto.getCategory());
        event.setName(travelEventRequestDto.getName());
        event.setDescription(travelEventRequestDto.getDescription());
        event.setPrice(travelEventRequestDto.getPrice());
        event.setStartTime(travelEventRequestDto.getStartTime());
        event.setEndTime(travelEventRequestDto.getEndTime());
        event.setTotalSeats(travelEventRequestDto.getTotalSeats());
        event.setFromLocation(travelEventRequestDto.getFromLocation());
        event.setToLocation(travelEventRequestDto.getToLocation());
        return event;
    }

    public Event toVenueEvent(VenueEventRequestDto venueEventRequestDto){
        Event event = new Event();
        event.setCategory(venueEventRequestDto.getCategory());
        event.setName(venueEventRequestDto.getName());
        event.setDescription(venueEventRequestDto.getDescription());
        event.setPrice(venueEventRequestDto.getPrice());
        event.setStartTime(venueEventRequestDto.getStartTime());
        event.setEndTime(venueEventRequestDto.getEndTime());
        event.setTotalSeats(venueEventRequestDto.getTotalSeats());
        event.setVenueLocation(venueEventRequestDto.getVenueLocation());
        return event;
    }

    public EventServiceGrpcResponseDto toFailedEventServiceGrpcResponseDto(){

        EventServiceGrpcResponseDto eventServiceGrpcResponseDto = new EventServiceGrpcResponseDto();

        eventServiceGrpcResponseDto.setExists(false);
        eventServiceGrpcResponseDto.setMessage("EventNotFound");

        return eventServiceGrpcResponseDto;

    }

    public EventServiceGrpcResponseDto toSuccessEventServiceGrpcResponseDto(Event event){

        EventServiceGrpcResponseDto eventServiceGrpcResponseDto = new EventServiceGrpcResponseDto();

        eventServiceGrpcResponseDto.setExists(true);
        eventServiceGrpcResponseDto.setEventName(event.getName());
        eventServiceGrpcResponseDto.setEventCategory(event.getCategory().toString());
        eventServiceGrpcResponseDto.setPrice(event.getPrice());
        eventServiceGrpcResponseDto.setMessage("EventFoundSuccessfully");

        return eventServiceGrpcResponseDto;
    }
}
