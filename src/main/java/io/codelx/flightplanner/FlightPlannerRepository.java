package io.codelx.flightplanner;

import io.codelx.flightplanner.domain.Flight;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightPlannerRepository {
    private final List<Flight> flights;

    public FlightPlannerRepository() {
        flights = new ArrayList<>();
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public void deleteFlight(Flight flight) {
        flights.remove(flight);
    }

    public List<Flight> flightsList() {
        return flights;
    }

    public void clear() {
        flights.clear();
    }
}
