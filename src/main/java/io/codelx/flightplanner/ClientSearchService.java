package io.codelx.flightplanner;

import io.codelx.flightplanner.domain.Airport;
import io.codelx.flightplanner.domain.Flight;
import io.codelx.flightplanner.dto.FlightDTO;
import io.codelx.flightplanner.request.CreateSearchFlightsRequest;
import io.codelx.flightplanner.response.PageResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ClientSearchService {
    private final FlightPlannerRepository flightPlannerRepository;
    private final FlightPlannerService flightPlannerService;

    public ClientSearchService(FlightPlannerRepository flightPlannerRepository, FlightPlannerService flightPlannerService) {
        this.flightPlannerRepository = flightPlannerRepository;
        this.flightPlannerService = flightPlannerService;
    }

    public List<Airport> searchAirports(String phrase) {

        List<Airport> fromAirports = flightPlannerRepository.flightsList().stream().map(Flight::getFrom).toList();
        List<Airport> toAirports = flightPlannerRepository.flightsList().stream().map(Flight::getTo).toList();

        List<Airport> allAirports = Stream.concat(fromAirports.stream(), toAirports.stream()).distinct().toList();

        return allAirports.stream()
                .filter(airport -> Airport.airportFieldFunctions.stream()
                        .anyMatch(func -> func.apply(airport).toLowerCase().contains(phrase.trim().toLowerCase())))
                .toList();
    }

    public PageResultResponse<Flight> searchFlight(CreateSearchFlightsRequest request) {

        if (request.getFrom().equals(request.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "same airports in searchRequest");
        }

        LocalDate requestDate = LocalDate.parse(request.getDepartureDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<Flight> foundFlights = flightPlannerRepository.flightsList().stream().filter(flight ->
                flight.getFrom().getAirport().equals(request.getFrom()) &&
                        flight.getTo().getAirport().equals(request.getTo()) &&
                        flight.getDepartureTime().toLocalDate().isEqual(requestDate)
        ).toList();

        return new PageResultResponse<>(0, foundFlights.size(), foundFlights);
    }

    public FlightDTO findFlightByID(Integer id) {
        Flight flight = flightPlannerService.fetchFlight(id);
        return new FlightDTO(flight.getId(), flight.getFrom(),
                flight.getTo(), flight.getCarrier(),
                flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                flight.getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
    }
}
