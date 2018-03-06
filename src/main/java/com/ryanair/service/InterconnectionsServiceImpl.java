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

    private final Map<String, Set<String>> directRoutes = new HashMap<>();
    private final ConcurrentHashMap<String, List<AirportSchedule>> routeSchedules = new ConcurrentHashMap<>();

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

    private List<AirportSchedule> getSchedules(String departureAirport, String arrivalAirport, int year, int month) {
        String key = departureAirport + "/" + arrivalAirport + "/" + year + "/" + month;
        List<AirportSchedule> schedules = routeSchedules.get(key);
        if (schedules != null) {
            return schedules;
        }
        schedules = new ArrayList<>();

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

    private Set getIntermediateAirports(Set<String> directFlights, String arrivalAirport) {
        return directFlights.stream().filter((String arrival) ->
            directRoutes.containsKey(arrival) && directRoutes.get(arrival).contains(arrivalAirport)
        ).collect(Collectors.toSet());
    }

    private List<AirportSchedule> filterSchedules(List<AirportSchedule> schedules, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return schedules.stream().filter((AirportSchedule schedule) ->
            schedule.getDepartureDateTime().isAfter(departureDateTime) && schedule.getArrivalDateTime().isBefore(arrivalDateTime)
        ).collect(Collectors.toList());
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

        LocalDate date = departureDateTime.toLocalDate();
        LocalDate arrivalDate = arrivalDateTime.plusMonths(1).toLocalDate();

        while (date.isBefore(arrivalDate)) {
            List<AirportSchedule> schedules = filterSchedules(getSchedules(departureAirport, arrivalAirport, date.getYear(), date.getMonthOfYear()), departureDateTime, arrivalDateTime);

            result.addAll(schedules.stream().map((AirportSchedule airportSchedule) -> {
                List<FlightLeg> legs = new LinkedList<>();
                legs.add(new FlightLeg(departureAirport, arrivalAirport, airportSchedule.getDepartureDateTime(), airportSchedule.getArrivalDateTime()));
                return new FlightRoute(legs);
            }).collect(Collectors.toList()));

            date = date.plusMonths(1);
        }

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