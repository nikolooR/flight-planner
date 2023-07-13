package io.codelx.flightplanner.request;

import jakarta.validation.constraints.NotEmpty;

public class CreateSearchFlightsRequest {
    @NotEmpty(message = "missing airport name")
    private String from;
    @NotEmpty(message = "missing airport name")
    private String to;
    @NotEmpty(message = "missing departure date")
    private String departureDate;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }
}
