package ru.pkorobeinikov.restservice.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.pkorobeinikov.restservice.util.BoundedRandomValueGenerator;

@JsonDeserialize(as = Point.class)
final public class Point {
    final private static int MIN = -100;
    final private static int MAX = 100;

    private int x;
    private int y;

    public Point() {
        super();
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point randomPoint() {
        int x = BoundedRandomValueGenerator.rand(MIN, MAX);
        int y = BoundedRandomValueGenerator.rand(MIN, MAX);

        return new Point(x, y);
    }

    @Override
    public String toString() {
        return String.format("Point{x=%d, y=%d}", x, y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
