package com.rezerve.rezerveeventservice.mapper;

import com.rezerve.rezerveeventservice.dto.response.EventResponseDto;
import com.rezerve.rezerveeventservice.dto.response.TravelEventResponseDto;
import com.rezerve.rezerveeventservice.dto.response.VenueEventResponseDto;
import com.rezerve.rezerveeventservice.model.Event;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
}
