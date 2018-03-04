package com.ryanair.service;

import com.ryanair.entity.Direction;
import com.ryanair.entity.MonthSchedule;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public interface RyanairApiService {
    List<Direction> requestDirections() throws IOException;
    MonthSchedule requestMonthSchedule(String fromAirport, String toAirport, int month, int year) throws IOException;
}
