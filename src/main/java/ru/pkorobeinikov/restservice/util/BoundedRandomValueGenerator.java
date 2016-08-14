package ru.pkorobeinikov.restservice.util;

final public class BoundedRandomValueGenerator {
    public static int rand(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
