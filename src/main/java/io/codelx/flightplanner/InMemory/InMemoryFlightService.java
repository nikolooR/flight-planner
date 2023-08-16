package io.codelx.flightplanner.InMemory;

import io.codelx.flightplanner.domain.Airport;
import io.codelx.flightplanner.domain.Flight;
import io.codelx.flightplanner.dto.FlightDTO;
import io.codelx.flightplanner.request.CreateSearchFlightsRequest;
import io.codelx.flightplanner.request.FlightRequest;
import io.codelx.flightplanner.response.PageResultResponse;
import io.codelx.flightplanner.services.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class InMemoryFlightService implements FlightService {
    Logger logger = LoggerFactory.getLogger(InMemoryFlightService.class);
    private final InMemoryFlightRepository inMemoryFlightRepository;

    public InMemoryFlightService(InMemoryFlightRepository inMemoryFlightRepository) {
        this.inMemoryFlightRepository = inMemoryFlightRepository;
    }

    public synchronized FlightDTO addFlight(FlightRequest request) {
        int id = inMemoryFlightRepository.flightsList().stream().mapToInt(Flight::getId).max().orElse(0);
        Flight newFlight = new Flight(
                id + 1
                , request.getFrom()
                , request.getTo()
                , request.getCarrier()
                , request.getDepartureTime()
                , request.getArrivalTime()
        );

        if (inMemoryFlightRepository.flightsList().stream().anyMatch(f -> f.equals(newFlight))) {
            logger.error("can not put same flight");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already have this flight");
        } else if (newFlight.getAirportFrom().equals(newFlight.getAirportTo())) {
            logger.error("same airports for departure and arrival");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Two identical airports");
        } else if (newFlight.getDepartureTime().equals(newFlight.getArrivalTime()) || newFlight.getDepartureTime().isAfter(newFlight.getArrivalTime())) {
            logger.error("Incorrect date time either equal or departure time is after arrival time");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect date time");
        } else {
            inMemoryFlightRepository.addFlight(newFlight);
            return new FlightDTO(
                    newFlight.getId(),
                    newFlight.getAirportFrom(),
                    newFlight.getAirportTo(),
                    newFlight.getCarrier(),
                    newFlight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    newFlight.getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
        }
    }

    public Flight fetchFlight(Integer id) {
        Optional<Flight> f = inMemoryFlightRepository.flightsList().stream()
                .filter(flight -> flight.getId().equals(id))
                .findFirst();

        if (f.isPresent()) {
            return f.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "flight does not exist");
        }
    }

    public synchronized void deleteFlight(Integer id) {
        inMemoryFlightRepository.flightsList().stream()
                .filter(flight -> flight.getId().equals(id))
                .findFirst().ifPresent(inMemoryFlightRepository::deleteFlight);
    }

    public List<Flight> getFlights() {
        return inMemoryFlightRepository.flightsList();
    }

    public void clear() {
        inMemoryFlightRepository.clear();
    }

    @Override
    public List<Airport> searchAirports(String phrase) {
        List<Airport> fromAirports = inMemoryFlightRepository.flightsList().stream().map(Flight::getAirportFrom).toList();
        List<Airport> toAirports = inMemoryFlightRepository.flightsList().stream().map(Flight::getAirportTo).toList();

        List<Airport> allAirports = Stream.concat(fromAirports.stream(), toAirports.stream()).distinct().toList();

        return allAirports.stream()
                .filter(airport -> Airport.airportFieldFunctions.stream()
                        .anyMatch(func -> func.apply(airport).toLowerCase().contains(phrase.trim().toLowerCase())))
                .toList();
    }

    @Override
    public PageResultResponse<Flight> searchFlights(CreateSearchFlightsRequest request) {
        if (request.getFrom().equals(request.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "same airports in searchRequest");
        }

        LocalDate requestDate = LocalDate.parse(request.getDepartureDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<Flight> flights = inMemoryFlightRepository.flightsList().stream().filter(flight ->
                flight.getAirportFrom().getAirport().equals(request.getFrom()) &&
                        flight.getAirportTo().getAirport().equals(request.getTo()) &&
                        flight.getDepartureTime().toLocalDate().isEqual(requestDate)
        ).toList();

        return new PageResultResponse<>(0, flights.size(), flights);
    }

    @Override
    public FlightDTO findFlightByID(Integer id) {
        Flight flight = fetchFlight(id);
        return new FlightDTO(
                flight.getId(),
                flight.getAirportFrom(),
                flight.getAirportTo(), flight.getCarrier(),
                flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                flight.getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
    }
}
