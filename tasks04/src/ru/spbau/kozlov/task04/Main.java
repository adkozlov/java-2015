package ru.spbau.kozlov.task04;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.compiler.StringJavaSourceCompiler;
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
            Scanner.class,
            Main.class
    };

    public static void main(@NonNull String[] args) {
        for (Class<?> clazz : TESTS) {
            String source;
            try {
                source = Reflector.printStructure(clazz);
            } catch (IOException e) {
                System.err.println("I/O error occurred: " + e.getMessage());
                continue;
            }

            try {
                StringJavaSourceCompiler.compileAndLoad(clazz.getSimpleName(), source);
            } catch (IOException e) {
                System.err.println("Output directory not found: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + e.getMessage());
            } catch (SecurityException e) {
                System.err.println("Security exception: " + e.getMessage());
            }
        }
    }
}
