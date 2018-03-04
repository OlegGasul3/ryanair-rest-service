package com.ryanair.service;

import com.google.common.collect.Lists;
import com.ryanair.entity.Direction;
import com.ryanair.entity.FlightRoute;
import com.ryanair.entity.MonthSchedule;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
public class InterconnectionsServiceImpl implements InterconnectionsService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final RyanairApiService ryanairApiService;

    private Map<String, Set<String>> directRoutes = new HashMap<>();

    public InterconnectionsServiceImpl(RyanairApiService ryanairApiService) {
        this.ryanairApiService = ryanairApiService;
    }

    @PostConstruct
    public void init() {
        try {
            LOG.info("Loading routes...");
            List<Direction> directions = ryanairApiService.requestDirections();
            for (Direction direction : directions) {
                String airportFrom = direction.getAirportFrom();
                Set values = directRoutes.get(airportFrom);
                if (null == values) {
                    values = new HashSet<>();
                    directRoutes.put(airportFrom, values);
                }

                values.add(direction.getAirportTo());
            }
            LOG.info("DONE");
        } catch (IOException e) {
            LOG.info("Error while loading routes", e);
        }

    }

    @Override
    public List<FlightRoute> getFlights(String departureAirport, String arrivalAirport, LocalDate departureDateTime, LocalDate arrivalDateTime) throws IOException {
        List<FlightRoute> result = new LinkedList();
        Set directFlights = directRoutes.get(departureAirport);
        if (directFlights.isEmpty() || !directFlights.contains(arrivalAirport)) {
            return Collections.<FlightRoute>emptyList();
        }

        MonthSchedule monthSchedule = ryanairApiService.requestMonthSchedule(departureAirport, arrivalAirport, departureDateTime.dayOfMonth().get(), departureDateTime.year().get());

        departureDateTime.dayOfMonth();
        departureDateTime.year();

        // todo: find with 1 stop
        return null;
    }
}
