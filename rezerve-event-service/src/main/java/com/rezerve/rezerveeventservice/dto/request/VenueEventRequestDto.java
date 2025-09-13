package com.rezerve.rezerveeventservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenueEventRequestDto extends EventRequestDto{

    @NotBlank(message = "Venue Location is required for Venue event")
    private String venueLocation;
}
