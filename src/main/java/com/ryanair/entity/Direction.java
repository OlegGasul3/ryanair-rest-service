package com.ryanair.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties({"connectingAirport", "operator", "group", "newRoute", "seasonalRoute"})
public class Direction {
    private final String airportFrom;
    private final String airportTo;

    @JsonCreator
    public Direction(@JsonProperty("airportFrom") String airportFrom, @JsonProperty("airportTo") String airportTo) {
        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
    }
}
