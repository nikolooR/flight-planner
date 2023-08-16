package io.codelx.flightplanner.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateSearchFlightsRequest {
    @NotEmpty(message = "missing airport name")
    private String from;

    @NotEmpty(message = "missing airport name")
    private String to;

    @NotEmpty(message = "missing departure date")
    private String departureDate;
}
