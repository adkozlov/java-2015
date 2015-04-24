package ru.spbau.kozlov.task04;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.Reflector;

import java.io.IOException;
import java.util.*;

/**
 * @author adkozlov
 */
public class Main {

    private static final Class<?>[] TESTS = {
            Object.class,
            List.class,
            ArrayList.class,
            Map.class,
            TreeMap.class,
            Scanner.class
    };

    public static void main(@NonNull String[] args) {
        try {
            for (Class<?> clazz : TESTS) {
                Reflector.printStructure(clazz);
            }
        } catch (IOException e) {
            System.err.println("I/O error occurred: " + e.getMessage());
        }
    }
}
