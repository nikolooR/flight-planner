package io.codelx.flightplanner.Controllers;

import io.codelx.flightplanner.FlightPlannerService;
import io.codelx.flightplanner.domain.Flight;
import io.codelx.flightplanner.request.FlightRequest;
import io.codelx.flightplanner.dto.FlightDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin-api")
@RestController
public class AdminController {

    private final FlightPlannerService flightPlannerService;

    public AdminController(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PutMapping("/flights")
    public FlightDTO addFlight(@Valid @RequestBody FlightRequest request) {
        return flightPlannerService.addFlight(request);
    }

    @GetMapping("/flights/{id}")
    public Flight fetchFlight(@PathVariable @Min(1) Integer id) {
        return flightPlannerService.fetchFlight(id);
    }

    @DeleteMapping("/flights/{id}")
    public void deleteFlight(@PathVariable @Min(1) Integer id) {
        flightPlannerService.deleteFlight(id);
    }

    @GetMapping("flights/get")
    public List<Flight> getFlights() {
        return flightPlannerService.getFlights();
    }


}
