package io.codelx.flightplanner.dataBase;

import io.codelx.flightplanner.domain.Airport;
import io.codelx.flightplanner.domain.Flight;
import io.codelx.flightplanner.dto.FlightDTO;
import io.codelx.flightplanner.request.CreateSearchFlightsRequest;
import io.codelx.flightplanner.request.FlightRequest;
import io.codelx.flightplanner.response.PageResultResponse;
import io.codelx.flightplanner.services.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

public class DBFlightService implements FlightService {

    Logger logger = LoggerFactory.getLogger(DBFlightService.class);

    private final DataBaseFlightRepository flightRepository;
    private final DataBaseAirportRepository airportRepository;

    public DBFlightService(DataBaseFlightRepository flightRepository, DataBaseAirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public synchronized FlightDTO addFlight(FlightRequest request) {

        Flight newFlight = new Flight(
                0
                , request.getFrom()
                , request.getTo()
                , request.getCarrier()
                , request.getDepartureTime()
                , request.getArrivalTime()
        );
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("airportFrom", exact())
                .withMatcher("airportTo", exact())
                .withMatcher("carrier", exact())
                .withMatcher("departureTime", exact())
                .withMatcher("arrivalTime", exact());

        Example<Flight> exampleFlight = Example.of(newFlight, exampleMatcher);

        if (flightRepository.exists(exampleFlight)) {
            logger.error("can not put same flight");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already have this flight");
        } else if (newFlight.getAirportFrom().equals(newFlight.getAirportTo())) {
            logger.error("same airports for departure and arrival");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Two identical airports");
        } else if (newFlight.getDepartureTime().equals(newFlight.getArrivalTime()) || newFlight.getDepartureTime().isAfter(newFlight.getArrivalTime())) {
            logger.error("Incorrect date time either equal or departure time is after arrival time");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect date time");
        } else {

            airportRepository.findById(newFlight.getAirportFrom().getAirport())
                    .ifPresentOrElse(newFlight::setAirportFrom, () -> airportRepository.save(newFlight.getAirportFrom()));

            airportRepository.findById(newFlight.getAirportTo().getAirport())
                    .ifPresentOrElse(newFlight::setAirportTo, () -> airportRepository.save(newFlight.getAirportTo()));

            Flight flight = flightRepository.save(newFlight);

            return new FlightDTO(
                    flight.getId(),
                    flight.getAirportFrom(),
                    flight.getAirportTo(),
                    flight.getCarrier(),
                    flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    flight.getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
        }
    }

    @Override
    public Flight fetchFlight(Integer id) {
        Optional<Flight> flight = flightRepository.findById(id);
        if (flight.isPresent()) {
            return flight.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "flight doesn't exist");
        }
    }

    @Override
    public synchronized void deleteFlight(Integer id) {
        flightRepository.deleteById(id);
    }

    @Override
    public List<Flight> getFlights() {
        return flightRepository.findAll();
    }

    @Override
    public void clear() {
        flightRepository.deleteAll();
        airportRepository.deleteAll();
    }

    @Override
    public List<Airport> searchAirports(String phrase) {
        return airportRepository.findAirportsByPhrase(phrase.trim().toLowerCase());
    }

    @Override
    public PageResultResponse<Flight> searchFlights(CreateSearchFlightsRequest request) {
        if (request.getFrom().equals(request.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "same airports in searchRequest");
        }
        LocalDate requestDate = LocalDate.parse(request.getDepartureDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Flight> flights =  flightRepository.findFlightsByAirportFromEqualsAndAirportToEqualsAndDepartureTimeEquals(request.getFrom(), request.getTo(), requestDate);
        return new PageResultResponse<Flight>(0, flights.size(), flights);
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
