package com.ryanair.entity;

import org.joda.time.LocalDateTime;

public class FlightLeg {
    private final String departureAirport;
    private final String arrivalAirport;
    private final LocalDateTime departureDateTime;
    private final LocalDateTime arrivalDateTime;

    public FlightLeg(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getDepartureDateTime() {
        return departureDateTime.toString();
    }

    public String getArrivalDateTime() {
        return arrivalDateTime.toString();
    }
}
