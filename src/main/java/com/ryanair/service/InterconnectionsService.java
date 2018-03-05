package com.ryanair.service;

import com.ryanair.entity.FlightRoute;
import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.util.List;

public interface InterconnectionsService {
    List<FlightRoute> getFlights(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) throws IOException;
}
