package ru.pkorobeinikov.restservice.controller.rest;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.pkorobeinikov.restservice.entity.*;

import java.util.ArrayList;
import java.util.List;

// HTTP status codes by RFC2616:
// https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.7

@RestController
@RequestMapping("/people/")
public class PeopleController {
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Person>> index(@RequestParam(value = "radius") int radius) {
        Person p1 = new Person("person-1");
        Person p2 = new Person("person-2");
        Person p3 = new Person("person-3");

        List<Person> l = new ArrayList<Person>();
        l.add(p1);
        l.add(p2);
        l.add(p3);

        return new ResponseEntity<List<Person>>(l, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Person> putPerson(@RequestBody Person person) {
        return new ResponseEntity<Person>(person, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{personId}", method = RequestMethod.DELETE)
    public ResponseEntity<Person> deletePerson(@PathVariable String personId) {
        Person p = new Person(personId);

        return new ResponseEntity<Person>(p, HttpStatus.OK);
    }

    @RequestMapping(value = "{personId}/coordinates", method = RequestMethod.GET)
    public ResponseEntity<Point> getPersonCoordinates(@PathVariable String personId) {
        Point p = new Point(1, 2);

        return new ResponseEntity<Point>(p, HttpStatus.OK);
    }
}
