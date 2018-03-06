package com.ryanair.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanair.entity.Direction;
import com.ryanair.entity.MonthSchedule;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class RyanairApiServiceImpl implements RyanairApiService {

    private String makeGetRequest(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch (Throwable t) {
            return null;
        }
    }

    private List<Direction> parseDirections(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Direction> directions = mapper.readValue(json, new TypeReference<List<Direction>>(){});
        return directions;
    }

    private MonthSchedule parseSchedule(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MonthSchedule monthSchedule = mapper.readValue(json, new TypeReference<MonthSchedule>(){});
        return monthSchedule;
    }

    @Override
    public List<Direction> requestDirections() throws IOException {
        String response = makeGetRequest("https://api.ryanair.com/core/3/routes/");
        return parseDirections(response);
    }

    @Override
    public MonthSchedule requestMonthSchedule(String fromAirport, String toAirport, int year, int month) throws IOException {
        String response = makeGetRequest("https://api.ryanair.com/timetable/3/schedules/" + fromAirport + "/" + toAirport + "/years/" + year + "/months/" + month);
        return parseSchedule(response);
    }
}
