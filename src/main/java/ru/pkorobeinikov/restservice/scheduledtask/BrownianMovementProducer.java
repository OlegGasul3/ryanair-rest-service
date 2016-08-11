package ru.pkorobeinikov.restservice.scheduledtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.pkorobeinikov.restservice.entity.*;
import ru.pkorobeinikov.restservice.repository.PersonRepository;

import java.util.List;

// see https://spring.io/guides/gs/scheduling-tasks/

@Component
final public class BrownianMovementProducer {
    final private PersonRepository personRepository;

    @Autowired
    public BrownianMovementProducer(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Scheduled(cron = "*/1 * * * * *")
    public void produceMovement() {
        List<Person> people = personRepository.findAll();
        for (Person p : people) {
            p.setPoint(Point.randomPoint());
        }
    }
}
