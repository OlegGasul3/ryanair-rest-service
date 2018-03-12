package com.ryanair.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.util.List;

@Getter
@JsonIgnoreProperties({"code", "message"})
public class MonthSchedule {
    private int month;
    private List<Day> days;

    public MonthSchedule(@JsonProperty("month") int month, @JsonProperty("days") List<Day> days) {
        this.month = month;
        this.days = days;
    }
}
