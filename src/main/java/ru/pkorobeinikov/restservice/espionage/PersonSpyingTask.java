package ru.pkorobeinikov.restservice.espionage;

import ru.pkorobeinikov.restservice.entity.Person;
import ru.pkorobeinikov.restservice.entity.Point;

import java.util.concurrent.Callable;

final public class PersonSpyingTask implements Callable<String> {
    private Person person;
    private Point center;
    private int radius;

    public PersonSpyingTask(Person person, Point center, int radius) {
        this.person = person;
        this.center = center;
        this.radius = radius;
    }

    @Override
    public String call() throws InterruptedException {
        boolean isBelongs;
        while (true) {
            Point p = person.getPoint();

            isBelongs = Math.pow(p.getY() - center.getY(), 2) + Math.pow(p.getX() - center.getX(), 2) <= Math.pow(radius, 2);
            if (isBelongs) {
                return String.format("%s found in center %s with radius %s.", person, center, radius);
            }

            Thread.sleep(1000);
        }
    }
}
