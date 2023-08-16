package io.codelx.flightplanner.Controllers;

import io.codelx.flightplanner.response.PageResultResponse;
import io.codelx.flightplanner.services.FlightService;
import io.codelx.flightplanner.domain.Airport;
import io.codelx.flightplanner.domain.Flight;
import io.codelx.flightplanner.dto.FlightDTO;
import io.codelx.flightplanner.request.CreateSearchFlightsRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class CustomerController {

    private FlightService flightService;

    public CustomerController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/airports")
    public List<Airport> searchAirports(@RequestParam String search) {
        return flightService.searchAirports(search);
    }

    @PostMapping("/flights/search")
    public PageResultResponse<Flight> searchFlights(@Valid @RequestBody CreateSearchFlightsRequest request) {
        return flightService.searchFlights(request);
    }

    @GetMapping("/flights/{id}")
    public FlightDTO findFlightById(@PathVariable Integer id) {
        return flightService.findFlightByID(id);
    }

}
