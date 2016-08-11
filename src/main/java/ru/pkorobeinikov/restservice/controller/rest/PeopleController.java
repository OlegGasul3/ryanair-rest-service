package ru.pkorobeinikov.restservice.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.pkorobeinikov.restservice.entity.*;
import ru.pkorobeinikov.restservice.exception.ResourceNotFoundException;
import ru.pkorobeinikov.restservice.repository.PersonRepository;

import java.util.List;

// HTTP status codes by RFC2616:
// https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.7

@RestController
@RequestMapping("/people/")
public class PeopleController {
    final private PersonRepository personRepository;

    @Autowired
    public PeopleController(PersonRepository personRepository) {
        this.personRepository = personRepository;

        // example data
        Person p1 = new Person("person-1").setPoint(new Point(1, 2));
        Person p2 = new Person("person-2").setPoint(new Point(3, 4));
        Person p3 = new Person("person-3").setPoint(new Point(5, 6));

        personRepository.add(p1);
        personRepository.add(p2);
        personRepository.add(p3);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Person>> index(@RequestParam(value = "radius") int radius) {
        List<Person> people = personRepository.findByRadius(radius);

        return new ResponseEntity<List<Person>>(people, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Person> putPerson(@RequestBody Person person) {
        if (null == person.getPoint()) {
            person.setPoint(Point.randomPoint());
        }

        personRepository.add(person);

        return new ResponseEntity<Person>(person, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{personId}", method = RequestMethod.DELETE)
    public ResponseEntity<Person> deletePerson(@PathVariable String personId) {
        if (null == personRepository.findById(personId)) {
            throw new ResourceNotFoundException();
        }

        Person p = personRepository.delete(personId);

        return new ResponseEntity<Person>(p, HttpStatus.OK);
    }

    @RequestMapping(value = "{personId}/coordinates", method = RequestMethod.GET)
    public ResponseEntity<Point> getPersonCoordinates(@PathVariable String personId) {
        Person p = personRepository.findById(personId);
        if (null == p) {
            throw new ResourceNotFoundException();
        }

        return new ResponseEntity<Point>(p.getPoint(), HttpStatus.OK);
    }
}
