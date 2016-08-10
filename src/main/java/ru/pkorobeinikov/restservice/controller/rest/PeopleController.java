package ru.pkorobeinikov.restservice.controller.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/people/")
public class PeopleController {
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam(value = "radius") int radius) {
        return "Find people in radius: " + radius;
    }

    @RequestMapping(value = "{personId}", method = RequestMethod.PUT)
    public String putPerson(@PathVariable String personId) {
        return String.format("Create new person with personId = %s", personId);
    }

    @RequestMapping(value = "{personId}", method = RequestMethod.DELETE)
    public String deletePerson(@PathVariable String personId) {
        return String.format("Delete person with personId = %s", personId);
    }

    @RequestMapping(value = "{personId}/coordinates", method = RequestMethod.GET)
    public String getPersonCoordinates(@PathVariable String personId) {
        return String.format("Get coordinates for person with personId = %s", personId);
    }
}
