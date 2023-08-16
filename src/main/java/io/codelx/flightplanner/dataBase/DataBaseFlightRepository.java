package io.codelx.flightplanner.dataBase;

import io.codelx.flightplanner.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DataBaseFlightRepository extends JpaRepository<Flight, Integer> {

    @Query(
            " SELECT f FROM Flight f " +
            " WHERE f.airportFrom.airport = :airportNameFrom " +
            " AND f.airportTo.airport = :airportNameTo AND DATE(f.departureTime) = :date"
    )
    public List<Flight> findFlightsByAirportFromEqualsAndAirportToEqualsAndDepartureTimeEquals(
            @Param("airportNameFrom") String from,
            @Param("airportNameTo") String to,
            @Param("date")LocalDate date
    );


}
