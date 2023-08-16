package io.codelx.flightplanner.services;

import io.codelx.flightplanner.domain.Airport;
import io.codelx.flightplanner.domain.Flight;
import io.codelx.flightplanner.dto.FlightDTO;
import io.codelx.flightplanner.request.CreateSearchFlightsRequest;
import io.codelx.flightplanner.response.PageResultResponse;

import java.util.List;

public interface ClientSearchService {
    List<Airport> searchAirports(String phrase);
    PageResultResponse<Flight> searchFlights(CreateSearchFlightsRequest request);
    FlightDTO findFlightByID(Integer id);
}
