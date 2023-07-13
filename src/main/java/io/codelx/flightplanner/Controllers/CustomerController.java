package io.codelx.flightplanner.Controllers;

import io.codelx.flightplanner.ClientSearchService;
import io.codelx.flightplanner.domain.Airport;
import io.codelx.flightplanner.domain.Flight;
import io.codelx.flightplanner.dto.FlightDTO;
import io.codelx.flightplanner.request.CreateSearchFlightsRequest;
import io.codelx.flightplanner.response.PageResultResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class CustomerController {

    private ClientSearchService clientSearchService;

    public CustomerController(ClientSearchService clientSearchService) {
        this.clientSearchService = clientSearchService;
    }

    @GetMapping("/airports")
    public List<Airport> searchAirports(@RequestParam String search) {
        return clientSearchService.searchAirports(search);
    }

    @PostMapping("/flights/search")
    public PageResultResponse<Flight> searchFlights(@Valid @RequestBody CreateSearchFlightsRequest request) {
        return clientSearchService.searchFlight(request);
    }

    @GetMapping("/flights/{id}")
    public FlightDTO findFlightById(@PathVariable Integer id) {
        return clientSearchService.findFlightByID(id);
    }

}
