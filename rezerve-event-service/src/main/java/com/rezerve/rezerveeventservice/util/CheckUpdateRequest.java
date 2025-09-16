package com.rezerve.rezerveeventservice.util;

import com.rezerve.rezerveeventservice.dto.request.EventUpdateRequestDto;
import com.rezerve.rezerveeventservice.exception.InvalidRequestBodyException;
import com.rezerve.rezerveeventservice.model.Event;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CheckUpdateRequest {

    public Event isUpdateRequestValid(Event oldEvent, EventUpdateRequestDto eventUpdateRequestDto){
        if(eventUpdateRequestDto.getName() != null){
            oldEvent.setName(eventUpdateRequestDto.getName());
        }
        if(eventUpdateRequestDto.getDescription() != null){
            oldEvent.setDescription(eventUpdateRequestDto.getDescription());
        }
        if(eventUpdateRequestDto.getPrice() != null){
            if(eventUpdateRequestDto.getPrice() < 0){
                throw new InvalidRequestBodyException("Price must be greater than 0");
            }
            oldEvent.setPrice(eventUpdateRequestDto.getPrice());
        }
        if(eventUpdateRequestDto.getStartTime() != null){
            if(!eventUpdateRequestDto.getStartTime().isAfter(LocalDateTime.now())){
                throw new InvalidRequestBodyException("Start time must be in future");
            }
            oldEvent.setStartTime(eventUpdateRequestDto.getStartTime());
        }
        if(eventUpdateRequestDto.getEndTime() != null){
            if(!eventUpdateRequestDto.getEndTime().isAfter(LocalDateTime.now()) || !eventUpdateRequestDto.getEndTime().isAfter(oldEvent.getStartTime())){
                throw new  InvalidRequestBodyException("End time must be in future and must be after start time");
            }
            oldEvent.setEndTime(eventUpdateRequestDto.getEndTime());
        }
        if(eventUpdateRequestDto.getTotalSeats() != null){
            if(eventUpdateRequestDto.getTotalSeats() < 0){
                throw new InvalidRequestBodyException("Total seats must be greater than 0");
            }
            oldEvent.setTotalSeats(eventUpdateRequestDto.getTotalSeats());
        }

        return oldEvent;
    }
}
