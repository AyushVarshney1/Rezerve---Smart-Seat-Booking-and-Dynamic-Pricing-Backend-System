package com.rezerve.rezerveeventservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelEventResponseDto extends EventResponseDto{
    private String fromLocation;
    private String toLocation;
}
