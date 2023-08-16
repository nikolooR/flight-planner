package io.codelx.flightplanner.Configuration;

import io.codelx.flightplanner.dataBase.DataBaseAirportRepository;
import io.codelx.flightplanner.dataBase.DataBaseFlightRepository;
import io.codelx.flightplanner.InMemory.InMemoryFlightRepository;
import io.codelx.flightplanner.dataBase.DBFlightService;
import io.codelx.flightplanner.services.FlightService;
import io.codelx.flightplanner.InMemory.InMemoryFlightService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "flight", name = "service.version", havingValue = "database")
    public FlightService getDataBaseVersion(DataBaseFlightRepository flightRepository, DataBaseAirportRepository airportRepository){
        return new DBFlightService(flightRepository, airportRepository);
    }

    @Bean
    @ConditionalOnProperty(prefix = "flight", name = "service.version", havingValue = "in-memory")
    public FlightService getInMemoryVersion(InMemoryFlightRepository repository){
        return new InMemoryFlightService(repository);
    }


}
