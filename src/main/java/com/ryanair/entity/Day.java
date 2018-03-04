package com.ryanair.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Day {
    private final int day;
    private final List<Flight> flights;

    public Day(@JsonProperty("day") int day, @JsonProperty("flights") List<Flight> flights) {
        this.day = day;
        this.flights = flights;
    }
}
