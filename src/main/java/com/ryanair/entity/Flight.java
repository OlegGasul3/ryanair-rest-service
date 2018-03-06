package com.ryanair.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
public class Flight {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private String number;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    public Flight(@JsonProperty("number") String number, @JsonProperty("departureTime") String departureTime, @JsonProperty("arrivalTime") String arrivalTime) {
        this.number = number;

        this.departureTime = LocalTime.parse(departureTime, formatter);
        this.arrivalTime = LocalTime.parse(arrivalTime, formatter);
    }
}
