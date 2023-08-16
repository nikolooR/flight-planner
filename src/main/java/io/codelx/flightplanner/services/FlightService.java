package io.codelx.flightplanner.services;

import io.codelx.flightplanner.domain.Flight;
import io.codelx.flightplanner.dto.FlightDTO;
import io.codelx.flightplanner.request.FlightRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FlightService extends ClientSearchService{
    FlightDTO addFlight(FlightRequest request);
    Flight fetchFlight(Integer id);
    void deleteFlight(Integer id);
    List<Flight> getFlights();
    void clear();
}
