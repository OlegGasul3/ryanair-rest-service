package ru.pkorobeinikov.restservice.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = Person.class)
final public class Person {
    private String id;
    private Point point;

    public Person() {
        super();
    }

    public Person(String id) {
        this.id = id;
    }

    public Person(String id, Point point) {
        this.id = id;
        this.point = point;
    }

    public String getId() {
        return id;
    }

    public Person setId(String id) {
        this.id = id;

        return this;
    }

    public Point getPoint() {
        return point;
    }

    public Person setPoint(Point point) {
        this.point = point;

        return this;
    }
}
