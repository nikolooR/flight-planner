package io.codelx.flightplanner.Controllers;

import io.codelx.flightplanner.domain.Flight;
import io.codelx.flightplanner.request.FlightRequest;
import io.codelx.flightplanner.dto.FlightDTO;
import io.codelx.flightplanner.services.FlightService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin-api")
@RestController
public class AdminController {

    private final FlightService flightService;

    public AdminController(FlightService flightService) {
        this.flightService = flightService;
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PutMapping("/flights")
    public FlightDTO addFlight(@Valid @RequestBody FlightRequest request) {
        return flightService.addFlight(request);
    }

    @GetMapping("/flights/{id}")
    public Flight fetchFlight(@PathVariable @Min(1) Integer id) {
        return flightService.fetchFlight(id);
    }

    @DeleteMapping("/flights/{id}")
    public void deleteFlight(@PathVariable @Min(1) Integer id) {
        flightService.deleteFlight(id);
    }

    @GetMapping("flights/get")
    public List<Flight> getFlights() {
        return flightService.getFlights();
    }


}
