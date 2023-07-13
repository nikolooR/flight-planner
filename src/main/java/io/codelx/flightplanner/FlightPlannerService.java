package io.codelx.flightplanner;

import io.codelx.flightplanner.domain.Flight;
import io.codelx.flightplanner.dto.FlightDTO;
import io.codelx.flightplanner.request.FlightRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class FlightPlannerService {
    Logger logger = LoggerFactory.getLogger(FlightPlannerService.class);
    private final FlightPlannerRepository flightPlannerRepository;

    public FlightPlannerService(FlightPlannerRepository flightPlannerRepository) {
        this.flightPlannerRepository = flightPlannerRepository;
    }

    public synchronized FlightDTO addFlight(FlightRequest request) {
        int id = flightPlannerRepository.flightsList().stream().mapToInt(Flight::getId).max().orElse(0);
        Flight newFlight = new Flight(
                id + 1
                , request.getFrom()
                , request.getTo()
                , request.getCarrier()
                , request.getDepartureTime()
                , request.getArrivalTime()
        );

        if (flightPlannerRepository.flightsList().stream().anyMatch(f -> f.equals(newFlight))) {
            logger.error("can not put same flight");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already have this flight");
        } else if (newFlight.getFrom().equals(newFlight.getTo())) {
            logger.error("same airports for departure and arrival");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Two identical airports");
        } else if (newFlight.getDepartureTime().equals(newFlight.getArrivalTime()) || newFlight.getDepartureTime().isAfter(newFlight.getArrivalTime())) {
            logger.error("Incorrect date time either equal or departure time is after arrival time");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect date time");
        } else {
            flightPlannerRepository.addFlight(newFlight);
            return new FlightDTO(
                    newFlight.getId(),
                    newFlight.getFrom(),
                    newFlight.getTo(),
                    newFlight.getCarrier(),
                    newFlight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    newFlight.getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
        }
    }

    public Flight fetchFlight(Integer id) {
        Optional<Flight> f = flightPlannerRepository.flightsList().stream()
                .filter(flight -> flight.getId().equals(id))
                .findFirst();

        if (f.isPresent()) {
            return f.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "flight does not exist");
        }
    }

    public synchronized void deleteFlight(Integer id) {
        flightPlannerRepository.flightsList().stream()
                .filter(flight -> flight.getId().equals(id))
                .findFirst().ifPresent(flight -> flightPlannerRepository.deleteFlight(flight));
    }

    public List<Flight> getFlights() {
        return flightPlannerRepository.flightsList();
    }

    public void clear() {
        flightPlannerRepository.clear();
    }

}
