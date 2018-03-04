package com.ryanair.service;

import com.ryanair.entity.Direction;
import com.ryanair.entity.FlightRoute;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public interface InterconnectionsService {
    List<FlightRoute> getFlights(String departureAirport, String arrivalAirport, LocalDate departureDateTime, LocalDate arrivalDateTime) throws IOException;
}
