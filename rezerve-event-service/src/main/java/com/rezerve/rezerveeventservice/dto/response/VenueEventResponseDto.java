package com.rezerve.rezerveeventservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenueEventResponseDto extends EventResponseDto{
    private String venueLocation;
}
