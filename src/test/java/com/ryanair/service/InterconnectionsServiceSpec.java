package com.ryanair.service;

import com.ryanair.entity.*;
import static com.ryanair.util.Airport.*;
import org.assertj.core.util.Lists;
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
    private final String departureAirport = DUB.toString();
    private final String connectAirport = KBP.toString();
    private final String arrivalAirport = WRO.toString();

    RyanairApiServiceImpl ryanairApiService = mock(RyanairApiServiceImpl.class);

    @Before
    public void prepareFlights() throws IOException {
        List<Direction> directions = new LinkedList<>();
        directions.add(new Direction(departureAirport, arrivalAirport));
        directions.add(new Direction(departureAirport, connectAirport));
        directions.add(new Direction(connectAirport, arrivalAirport));

        when(ryanairApiService.requestDirections()).thenReturn(directions);

        List<Flight> flights = new LinkedList<>();

        flights.add(new Flight("1982", "10:00", "12:00"));
        flights.add(new Flight("1982", "14:00", "16:00"));

        List<Day> days = IntStream.range(1, 5).mapToObj((i) -> {
            return new Day(i, flights);
        }).collect(Collectors.toList());

        IntStream.range(7, 10).forEach((i) -> {
            MonthSchedule monthSchedule = new MonthSchedule(i, days);
            try {
                when(ryanairApiService.requestMonthSchedule(departureAirport, arrivalAirport, 2018, i)).thenReturn(monthSchedule);
                when(ryanairApiService.requestMonthSchedule(departureAirport, connectAirport, 2018, i)).thenReturn(monthSchedule);
                when(ryanairApiService.requestMonthSchedule(connectAirport, departureAirport, 2018, i)).thenReturn(monthSchedule);
            } catch (IOException e) {
                // ignore
            }
        });
    }

    private void setFlight(String departureAirport, String arrivalAirport) {

    }

    @Test
    public void testDirectFlights() throws IOException {
        InterconnectionsServiceImpl interconnectionsService = new InterconnectionsServiceImpl(ryanairApiService);
        interconnectionsService.init();
        List<FlightRoute> result = interconnectionsService.getFlights(departureAirport, arrivalAirport, LocalDateTime.parse("2018-07-01"), LocalDateTime.parse("2018-07-03"));

        assertEquals(8, result.size());

        assertEquals(1, result.get(0).getLegs().size());
        assertEquals(0, result.get(0).getStops());

        assertEquals(1, result.get(1).getLegs().size());
        assertEquals(0, result.get(1).getStops());
    }

    @Test
    public void testDirectionsNotFound() throws IOException {
        when(ryanairApiService.requestDirections()).thenReturn(Lists.emptyList());

        InterconnectionsServiceImpl interconnectionsService = new InterconnectionsServiceImpl(ryanairApiService);
        interconnectionsService.init();

        List<FlightRoute> result = interconnectionsService.getFlights(departureAirport, arrivalAirport, LocalDateTime.parse("2018-07-01"), LocalDateTime.parse("2018-07-03"));
        assertEquals(0, result.size());
    }
}
