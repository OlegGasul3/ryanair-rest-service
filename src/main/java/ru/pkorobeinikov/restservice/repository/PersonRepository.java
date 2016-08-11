package ru.pkorobeinikov.restservice.repository;

import org.springframework.stereotype.Service;
import ru.pkorobeinikov.restservice.entity.Person;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class PersonRepository {
    private static ConcurrentHashMap<String, Person> personStorage = new ConcurrentHashMap<>();

    public Person add(Person person) {
        personStorage.put(person.getId(), person);

        return person;
    }

    public Person delete(String id) {
        Person p = personStorage.get(id);
        personStorage.remove(id);

        return p;
    }

    public Person findById(String id) {
        return personStorage.get(id);
    }

    public List<Person> findByRadius(int radius) {
        return personStorage
                .values()
                .stream()
                .filter(p -> Math.pow(p.getPoint().getX(), 2) + Math.pow(p.getPoint().getY(), 2) <= Math.pow(radius, 2))
                .collect(Collectors.toList());
    }
}
