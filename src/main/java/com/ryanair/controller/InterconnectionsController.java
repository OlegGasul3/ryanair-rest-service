package com.ryanair.controller;

import com.ryanair.entity.Flight;
import com.ryanair.service.InterconnectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/flights/")
public class InterconnectionsController {
    final private InterconnectionsService routeService;

    @Autowired
    public InterconnectionsController(InterconnectionsService routeService) {
        this.routeService = routeService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Flight>> index(@RequestParam(value = "departure") String departureAirport,
                                              @RequestParam(value = "arrival") String arrivalAirport,
                                              @RequestParam String departureDateTime,
                                              @RequestParam String arrivalDateTime) {
        List<Flight> flights = new LinkedList<>();

        return new ResponseEntity<>(flights, HttpStatus.OK);
    }
}
