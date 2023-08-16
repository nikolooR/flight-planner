package io.codelx.flightplanner.InMemory;

import io.codelx.flightplanner.domain.Flight;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryFlightRepository {
    private final List<Flight> flights;

    public InMemoryFlightRepository() {
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
