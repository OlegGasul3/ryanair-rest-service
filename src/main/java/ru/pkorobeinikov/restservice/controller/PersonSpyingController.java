package ru.pkorobeinikov.restservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.pkorobeinikov.restservice.entity.Point;
import ru.pkorobeinikov.restservice.espionage.PersonSpyingService;

import java.util.concurrent.ExecutionException;

@Controller
public class PersonSpyingController {
    final private PersonSpyingService personSpyingService;

    @Autowired
    public PersonSpyingController(PersonSpyingService personSpyingService) {
        this.personSpyingService = personSpyingService;
    }

    @ResponseBody
    @RequestMapping(value = "/spy", method = RequestMethod.GET)
    public String spy() {
        String out = "";

        String personId = "person-1";
        //Point point = Point.randomPoint();
        Point point = new Point(0, 0);
        int radius = 10;
        int timeoutSec = 5;

        try {
            out = personSpyingService.spyOnThePerson(personId, point, radius, timeoutSec);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return String.format("Espionage on %s returns: %s", personId, out);
    }
}
