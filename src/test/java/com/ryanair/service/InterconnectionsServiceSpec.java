package com.ryanair.service;

import com.ryanair.entity.*;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InterconnectionsServiceSpec {
    private final String departureAirport = "DUB";
    private final String arrivalAirport = "WRO";

    RyanairApiServiceImpl ryanairApiService = mock(RyanairApiServiceImpl.class);

    @Before
    public void init() throws IOException {
        List<Direction> directions = new LinkedList<>();
        directions.add(new Direction(departureAirport, departureAirport));

        when(ryanairApiService.requestDirections()).thenReturn(directions);

        List<Flight> flights = new LinkedList<>();

        flights.add(new Flight("1982", "10:00", "12:00"));

        List<Day> days = IntStream.range(1, 5).mapToObj((i) -> {
            return new Day(i, flights);
        }).collect(Collectors.toList());

        MonthSchedule monthSchedule = new MonthSchedule(7, days);
        when(ryanairApiService.requestMonthSchedule(departureAirport, arrivalAirport, 2018, 7)).thenReturn(monthSchedule);

        monthSchedule = new MonthSchedule(8, days);
        when(ryanairApiService.requestMonthSchedule(departureAirport, arrivalAirport, 2018, 8)).thenReturn(monthSchedule);
    }

    @Test
    public void testFlights() throws IOException {
        InterconnectionsServiceImpl interconnectionsService = new InterconnectionsServiceImpl(ryanairApiService);
        interconnectionsService.init();
        List<FlightRoute> result = interconnectionsService.getFlights(departureAirport, arrivalAirport, LocalDateTime.parse("2018-07-01"), LocalDateTime.parse("2018-07-03"));

        assertEquals(2, result.size());

        assertEquals(1, result.get(0).getLegs().size());
        assertEquals(0, result.get(0).getStops());

        assertEquals(1, result.get(1).getLegs().size());
        assertEquals(0, result.get(1).getStops());
    }
}
