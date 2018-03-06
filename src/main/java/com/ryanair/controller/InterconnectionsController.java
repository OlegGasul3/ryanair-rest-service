package com.ryanair.controller;

import com.ryanair.entity.FlightRoute;
import com.ryanair.service.InterconnectionsService;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class InterconnectionsController {
    private final InterconnectionsService routeService;

    @Autowired
    public InterconnectionsController(InterconnectionsService routeService) {
        this.routeService = routeService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<FlightRoute>> index(@RequestParam(value = "departure") String departureAirport,
                                                   @RequestParam(value = "arrival") String arrivalAirport,
                                                   @RequestParam String departureDateTime,
                                                   @RequestParam String arrivalDateTime) throws IOException {
        List<FlightRoute> flights = routeService.getFlights(departureAirport, arrivalAirport, new LocalDateTime(departureDateTime), new LocalDateTime(arrivalDateTime));

        return new ResponseEntity<>(flights, HttpStatus.OK);
    }
}
