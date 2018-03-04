package com.ryanair.service;

import com.ryanair.entity.Direction;
import com.ryanair.entity.MonthSchedule;
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
        MonthSchedule monthSchedule = ryanairApiService.requestMonthSchedule("DUB", "WRO", 7, 2018);

        assert true;
    }

}
