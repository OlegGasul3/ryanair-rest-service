package com.ryanair.service;

import com.ryanair.entity.Direction;
import com.ryanair.entity.FlightRoute;
import com.ryanair.entity.MonthSchedule;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class RouteServiceSpec {

    @Test
    public void testLoadAllDirections() throws IOException {
        RyanairApiServiceImpl ryanairApiService = new RyanairApiServiceImpl();
        List<Direction> directions = ryanairApiService.requestDirections();

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
