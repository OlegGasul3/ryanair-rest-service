package com.ryanair.entity;

import lombok.Getter;

import java.util.List;

@Getter
public class FlightRoute {
    private final List<FlightLeg> legs;

    public FlightRoute(List<FlightLeg> legs) {
        this.legs = legs;
    }

    public int getStops() {
        return legs.size() - 1;
    }
}
