package com.ryanair.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class MonthSchedule {
    private int month;
    private List<Day> days;

    public MonthSchedule(@JsonProperty("month") int month, @JsonProperty("days") List<Day> days) {
        this.month = month;
        this.days = days;
    }
}
