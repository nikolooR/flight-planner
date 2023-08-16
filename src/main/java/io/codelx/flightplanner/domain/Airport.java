package io.codelx.flightplanner.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Airport")
@Table(name = "airports")
public class Airport {
    @Valid

    @NotEmpty(message = "missing country")
    @Column(name = "country", nullable = false, columnDefinition = "VARCHAR")
    private String country;

    @NotEmpty(message = "missing city")
    @Column(name = "city", nullable = false, columnDefinition = "VARCHAR")
    private String city;

    @Id
    @NotEmpty(message = "missing airport name")
    @Column(name = "airport", nullable = false, columnDefinition = "VARCHAR")
    private String airport;

    public static List<Function<Airport, String>> airportFieldFunctions = List.of(Airport::getCountry, Airport::getCity, Airport::getAirport);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport1 = (Airport) o;
        return country.equalsIgnoreCase(airport1.country) && city.equalsIgnoreCase(airport1.city) && airport.equalsIgnoreCase(airport1.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, airport);
    }
}
