package com.ryanair.service;

import com.ryanair.entity.*;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InterconnectionsServiceSpec {

    @InjectMocks
    RyanairApiServiceImpl ryanairApiService;

    @Test
    public void testFlights() throws IOException {
        String departureAirport = "DUB";
        String arrivalAirport = "WRO";

        List<Direction> directions = new LinkedList<>();
        directions.add(new Direction(departureAirport, departureAirport));

        when(ryanairApiService.requestDirections()).thenReturn(directions);

        List<Day> days = new LinkedList<>();
        List<Flight> flights = new LinkedList<>();

        flights.add(new Flight("1982", "10:00", "12:00"));

        days.add(new Day(1, flights));
        days.add(new Day(2, flights));
        days.add(new Day(3, flights));
        MonthSchedule monthSchedule = new MonthSchedule(7, days);

        when(ryanairApiService.requestMonthSchedule(departureAirport, arrivalAirport, 2018, 7)).thenReturn(monthSchedule);

        InterconnectionsService interconnectionsService = new InterconnectionsServiceImpl(ryanairApiService);
        interconnectionsService.getFlights(departureAirport, arrivalAirport, LocalDateTime.parse("2018-07-01"), LocalDateTime.parse("2018-07-03"));

        assert true;
    }

    @Test
    public void testLoadSchedule() throws IOException {
        RyanairApiServiceImpl ryanairApiService = new RyanairApiServiceImpl();
        MonthSchedule monthSchedule = ryanairApiService.requestMonthSchedule("DUB", "WRO", 2018, 7);

        InterconnectionsServiceImpl interconnectionsService = new InterconnectionsServiceImpl(ryanairApiService);
        interconnectionsService.init();

        List<FlightRoute> result = interconnectionsService.getFlights("DUB", "WRO", new LocalDateTime(2018, 7, 1, 14, 0), new LocalDateTime(2018, 7, 1, 23, 0));

        assert true;
    }

}
