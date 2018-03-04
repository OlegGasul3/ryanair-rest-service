package com.ryanair.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Direction {
    private final String airportFrom;
    private final String airportTo;
    private final String connectingAirport;
    private final String operator;
    private final String group;
    private final boolean newRoute;
    private final boolean seasonalRoute;

    @JsonCreator
    public Direction(@JsonProperty("airportFrom") String airportFrom, @JsonProperty("airportTo") String airportTo, @JsonProperty("connectingAirport") String connectingAirport, @JsonProperty("operator") String operator, @JsonProperty("group") String group, @JsonProperty("newRoute") boolean newRoute, @JsonProperty("seasonalRoute") boolean seasonalRoute) {
        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
        this.connectingAirport = connectingAirport;
        this.operator = operator;
        this.group = group;
        this.newRoute = newRoute;
        this.seasonalRoute = seasonalRoute;
    }
}
