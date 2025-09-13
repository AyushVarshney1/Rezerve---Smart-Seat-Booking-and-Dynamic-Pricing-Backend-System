package com.rezerve.rezerveeventservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelEventRequestDto extends EventRequestDto{

    @NotBlank(message = "From Location is required for travel event")
    private String fromLocation;
    @NotBlank(message = "To Location is required for travel event")
    private String toLocation;
}
