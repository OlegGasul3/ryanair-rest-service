package com.ryanair.entity;

import lombok.Getter;
import org.joda.time.LocalDate;

@Getter
public class FlightLeg {
    private final String departureAirport;
    private final String arrivalAirport;
    private final LocalDate departureDateTime;
    private final LocalDate arrivalDateTime;

    public FlightLeg(String departureAirport, String arrivalAirport, LocalDate departureDateTime, LocalDate arrivalDateTime) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
    }
}
