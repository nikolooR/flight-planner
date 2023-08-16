package io.codelx.flightplanner.Controllers;

import io.codelx.flightplanner.services.FlightService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/testing-api")
@RestController
public class TestingClientController {
    private final FlightService flightService;

    public TestingClientController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/clear")
    public void clear() {
        flightService.clear();
    }

}
