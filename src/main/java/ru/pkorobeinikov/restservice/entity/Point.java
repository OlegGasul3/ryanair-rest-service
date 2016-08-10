package ru.pkorobeinikov.restservice.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
        return new Point(rand(MIN, MAX), rand(MIN, MAX));
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

    private static int rand(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
