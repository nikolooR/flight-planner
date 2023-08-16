package io.codelx.flightplanner.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.codelx.flightplanner.domain.Airport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightRequest {
    @Valid
    @NotNull(message = "missing airport from")
    private Airport from;

    @Valid
    @NotNull(message = "missing airport to")
    private Airport to;

    @NotEmpty(message = "missing carrier")
    private String carrier;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "missing departure time")
    private LocalDateTime departureTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "missing arrival time")
    private LocalDateTime arrivalTime;

}
