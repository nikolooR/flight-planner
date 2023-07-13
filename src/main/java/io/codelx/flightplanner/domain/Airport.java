package io.codelx.flightplanner.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Airport {
    @Valid
    @NotEmpty(message = "missing country")
    private String country;
    @NotEmpty(message = "missing city")
    private String city;
    @NotEmpty(message = "missing airport name")
    private String airport;

    public static List<Function<Airport, String>> airportFieldFunctions = List.of(Airport::getCountry, Airport::getCity, Airport::getAirport);

    public Airport(String country, String city, String airport) {
        this.country = country;
        this.city = city;
        this.airport = airport;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }


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

    @Override
    public String toString() {
        return "Airport{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", airport='" + airport + '\'' +
                '}';
    }
}
