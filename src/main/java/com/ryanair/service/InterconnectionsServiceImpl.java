package com.ryanair.service;

import com.ryanair.entity.*;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
                if (values == null) {
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

    private void addSchedules(Map<String, List<AirportSchedule>> airportSchedules, String key, int year, MonthSchedule monthSchedule) {
        List<AirportSchedule> schedules = airportSchedules.get(key);
        if (schedules == null) {
            schedules = new ArrayList<>();
            airportSchedules.put(key, schedules);
        }

        int month = monthSchedule.getMonth();
        for (Day day : monthSchedule.getDays()) {
            int dayNumber = day.getDay();
            for (Flight flight : day.getFlights()) {
                LocalDateTime departureDateTime = new LocalDateTime(year, month, dayNumber, flight.getDepartureTime().getHour(), flight.getDepartureTime().getMinute());
                LocalDateTime arrivalDateTime = new LocalDateTime(year, month, dayNumber, flight.getArrivalTime().getHour(), flight.getArrivalTime().getMinute());
                schedules.add(new AirportSchedule(departureDateTime, arrivalDateTime));
            }
        }
    }

    private void fillAirportSchedules(Map<String, List<AirportSchedule>> airportSchedules, String departureAirport, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        String airportsKey = departureAirport + "/" + arrivalAirport;

        LocalDate date = departureDateTime.toLocalDate();
        LocalDate arrivalDate = arrivalDateTime.plusMonths(1).toLocalDate();

        while (date.isBefore(arrivalDate)) {
            int year = date.getYear();
            MonthSchedule monthSchedule = null;
            try {
                monthSchedule = ryanairApiService.requestMonthSchedule(departureAirport, arrivalAirport, year, date.getMonthOfYear());
                addSchedules(airportSchedules, airportsKey, year, monthSchedule);
            } catch (IOException e) {
                // ignore
            }

            date = date.plusMonths(1);
        }
    }

    private Set getIntermediateAirports(Set<String> directFlights, String arrivalAirport) {
        Set<String> result = new HashSet<>();
        for (String arrival : directFlights) {
            Set<String> routes = directRoutes.get(arrival);
            if (routes.contains(arrivalAirport)) {
                result.add(arrival);
            }
        }

        return result;
    }

    @Override
    public List<FlightRoute> getFlights(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) throws IOException {
        if (arrivalDateTime.isBefore(departureDateTime)) {
            return Collections.<FlightRoute>emptyList();
        }

        Set<String> directFlights = directRoutes.get(departureAirport);
        if (directFlights.isEmpty()) {
            return Collections.<FlightRoute>emptyList();
        }

        List<FlightRoute> result = new LinkedList<>();

        Map<String, List<AirportSchedule>> airportSchedules = new HashMap<>();
        fillAirportSchedules(airportSchedules, departureAirport, arrivalAirport, departureDateTime, arrivalDateTime);

        Set<String> airports = getIntermediateAirports(directFlights, arrivalAirport);
        for (String airport : airports) {
            fillAirportSchedules(airportSchedules, airport, arrivalAirport, departureDateTime, arrivalDateTime);
        }

        // todo: find all


        // todo: find with 1 stop
        return null;
    }
}

class AirportSchedule {
    private final LocalDateTime departureDateTime;
    private final LocalDateTime arrivalDateTime;

    AirportSchedule(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
    }
}