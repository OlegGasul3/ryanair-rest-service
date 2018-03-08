package com.ryanair.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanair.entity.Direction;
import com.ryanair.entity.MonthSchedule;
import com.ryanair.util.HttpUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RyanairApiServiceImpl implements RyanairApiService {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    private List<Direction> parseDirections(final String json) throws IOException {
        return MAPPER.readValue(json, new TypeReference<List<Direction>>(){});
    }

    private MonthSchedule parseSchedule(final String json) throws IOException {
        return MAPPER.readValue(json, new TypeReference<MonthSchedule>(){});
    }

    @Override
    public List<Direction> requestDirections() throws IOException {
        return parseDirections(HttpUtil.makeGetRequest("https://api.ryanair.com/core/3/routes/"));
    }

    @Override
    public MonthSchedule requestMonthSchedule(final String fromAirport, final String toAirport, int year, int month) throws IOException {
        return parseSchedule(HttpUtil.makeGetRequest("https://api.ryanair.com/timetable/3/schedules/" + fromAirport + "/" + toAirport + "/years/" + year + "/months/" + month));
    }
}
