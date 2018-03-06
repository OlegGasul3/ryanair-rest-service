package com.ryanair.service;

import com.ryanair.entity.*;
import lombok.Getter;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class InterconnectionsServiceImpl implements InterconnectionsService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final RyanairApiService ryanairApiService;

    private final ConcurrentHashMap<String, Set<String>> directRoutes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<AirportSchedule>> routeSchedules = new ConcurrentHashMap<>();

    public InterconnectionsServiceImpl(RyanairApiService ryanairApiService) {
        this.ryanairApiService = ryanairApiService;
    }

    @PostConstruct
    public void init() {
        try {
            LOG.info("Loading direct routes...");
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
            LOG.info("Error while loading direct routes", e);
        }

    }

    private List<AirportSchedule> getSchedules(String departureAirport, String arrivalAirport, int year, int month) {
        String key = departureAirport + "/" + arrivalAirport + "/" + year + "/" + month;
        List<AirportSchedule> schedules = routeSchedules.get(key);
        if (schedules != null) {
            return schedules;
        }

        schedules = new LinkedList<>();

        try {
            MonthSchedule monthSchedule = ryanairApiService.requestMonthSchedule(departureAirport, arrivalAirport, year, month);

            for (Day day : monthSchedule.getDays()) {
                int dayNumber = day.getDay();
                schedules.addAll(day.getFlights().stream().map((Flight flight) -> {
                    LocalDateTime departureDateTime = new LocalDateTime(year, month, dayNumber, flight.getDepartureTime().getHour(), flight.getDepartureTime().getMinute());
                    LocalDateTime arrivalDateTime = new LocalDateTime(year, month, dayNumber, flight.getArrivalTime().getHour(), flight.getArrivalTime().getMinute());
                    return new AirportSchedule(departureDateTime, arrivalDateTime);
                }).collect(Collectors.toList()));
            }
        } catch (IOException e) {
            // ignore
        }

        routeSchedules.put(key, schedules);

        return schedules;
    }

    private List<AirportSchedule> getSchedules(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        LocalDate date = departureDateTime.toLocalDate();
        LocalDate arrivalDate = arrivalDateTime.plusMonths(1).toLocalDate();

        List<AirportSchedule> schedules = new LinkedList<>();
        while (date.isBefore(arrivalDate)) {
            schedules.addAll(filterSchedules(getSchedules(departureAirport, arrivalAirport, date.getYear(), date.getMonthOfYear()), departureDateTime, arrivalDateTime));

            date = date.plusMonths(1);
        }

        return schedules;
    }

    private List<AirportSchedule> filterSchedules(List<AirportSchedule> schedules, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return schedules.stream().filter((AirportSchedule schedule) ->
            schedule.getDepartureDateTime().isAfter(departureDateTime) && schedule.getArrivalDateTime().isBefore(arrivalDateTime)
        ).collect(Collectors.toList());
    }

    private List<FlightRoute> getInterconnectFlights(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        Set<String> directFlights = directRoutes.get(departureAirport);
        if (directFlights.isEmpty()) {
            return Collections.<FlightRoute>emptyList();
        }

        Set<String> connectAirports = directFlights.stream().filter((String airport) -> {
            Set<String> routes = directRoutes.get(airport);

            return routes != null && routes.contains(arrivalAirport);
        }).collect(Collectors.toSet());

        List<FlightRoute> result = new LinkedList<>();

        for (String connectAirport : connectAirports) {
            List<AirportSchedule> schedules = getSchedules(departureAirport, connectAirport, departureDateTime, arrivalDateTime);

            for (AirportSchedule schedule : schedules) {
                List<AirportSchedule> destSchedules = getSchedules(connectAirport, departureAirport, schedule.getArrivalDateTime().plusHours(2), arrivalDateTime);
                FlightLeg leg = new FlightLeg(departureAirport, connectAirport, schedule.getDepartureDateTime(), schedule.getArrivalDateTime());
                result.addAll(destSchedules.stream().map((AirportSchedule connectAirportSchedule) -> {
                    List<FlightLeg> legs = new LinkedList<>();
                    legs.add(leg);
                    legs.add(new FlightLeg(connectAirport, arrivalAirport, connectAirportSchedule.getDepartureDateTime(), connectAirportSchedule.getArrivalDateTime()));
                    return new FlightRoute(legs);
                }).collect(Collectors.toList()));
            }
        }

        return result;
    }

    @Override
    public List<FlightRoute> getFlights(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        if (arrivalDateTime.isBefore(departureDateTime)) {
            return Collections.<FlightRoute>emptyList();
        }

        Set<String> directFlights = directRoutes.get(departureAirport);
        if (directFlights.isEmpty()) {
            return Collections.<FlightRoute>emptyList();
        }

        List<FlightRoute> result = new LinkedList<>();

        result.addAll(getSchedules(departureAirport, arrivalAirport, departureDateTime, arrivalDateTime).stream().map((AirportSchedule airportSchedule) -> {
            List<FlightLeg> legs = new LinkedList<>();
            legs.add(new FlightLeg(departureAirport, arrivalAirport, airportSchedule.getDepartureDateTime(), airportSchedule.getArrivalDateTime()));
            return new FlightRoute(legs);
        }).collect(Collectors.toList()));

//        result.addAll(getInterconnectFlights(departureAirport, arrivalAirport, departureDateTime, arrivalDateTime));

        return result;
    }
}

@Getter
class AirportSchedule {
    private final LocalDateTime departureDateTime;
    private final LocalDateTime arrivalDateTime;

    AirportSchedule(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
    }
}