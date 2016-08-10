package ru.pkorobeinikov.restservice.repository;

import org.springframework.stereotype.Service;
import ru.pkorobeinikov.restservice.entity.Person;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PersonRepository {
    private static ConcurrentHashMap<String, Person> personStorage = new ConcurrentHashMap<String, Person>();

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

    public Enumeration<Person> findByRadius(int radius) {
        return personStorage.elements();
    }
}
