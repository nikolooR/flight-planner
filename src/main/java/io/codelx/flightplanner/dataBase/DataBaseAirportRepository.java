package io.codelx.flightplanner.dataBase;

import io.codelx.flightplanner.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataBaseAirportRepository extends JpaRepository<Airport, String> {
    @Query("SELECT a from Airport a WHERE LOWER(CONCAT(a.airport, ' ',  a.city, ' ', a.country) ) LIKE LOWER(('%' || :name || '%'))")
    public List<Airport> findAirportsByPhrase(@Param("name") String phrase);
}

