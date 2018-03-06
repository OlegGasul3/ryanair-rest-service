package com.ryanair.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanair.entity.Direction;
import com.ryanair.entity.MonthSchedule;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class RyanairApiServiceImpl implements RyanairApiService {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    private String makeGetRequest(final String url) throws IOException {
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

    private List<Direction> parseDirections(final String json) throws IOException {
        return MAPPER.readValue(json, new TypeReference<List<Direction>>(){});
    }

    private MonthSchedule parseSchedule(final String json) throws IOException {
        return MAPPER.readValue(json, new TypeReference<MonthSchedule>(){});
    }

    @Override
    public List<Direction> requestDirections() throws IOException {
        return parseDirections(makeGetRequest("https://api.ryanair.com/core/3/routes/"));
    }

    @Override
    public MonthSchedule requestMonthSchedule(final String fromAirport, final String toAirport, int year, int month) throws IOException {
        return parseSchedule(makeGetRequest("https://api.ryanair.com/timetable/3/schedules/" + fromAirport + "/" + toAirport + "/years/" + year + "/months/" + month));
    }
}
