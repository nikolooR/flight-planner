package io.codelx.flightplanner.Controllers;

import io.codelx.flightplanner.FlightPlannerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/testing-api")
@RestController
public class TestingClientController {
    private final FlightPlannerService flightPlannerService;

    public TestingClientController(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }

    @PostMapping("/clear")
    public void clear() {
        flightPlannerService.clear();
    }

}
