package ru.spbau.kozlov.task04;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.compiler.CompilationException;
import ru.spbau.kozlov.task04.compiler.Compiler;
import ru.spbau.kozlov.task04.reflector.Reflector;

import java.io.IOException;

/**
 * The {@link Main} reflects classes, that are passed as command-line parameters, then compiles them and shows any differences between the original classes and the compiled ones.
 *
 * @author adkozlov
 */
public class Main {

    private static final String[] CANONICAL_NAMES = {
            "java.lang.Object",
            "java.util.List",
            "java.util.TreeMap",
            "java.util.Scanner",
            "ru.spbau.kozlov.task04.Main"
    };

    /**
     * The command-line interface.
     * Classes should be represented with their canonical names.
     * If no class is passed then the following classes are used as tests:
     * 1) {@link Object};
     * 2) {@link java.util.List};
     * 3) {@link java.util.TreeMap};
     * 4) {@link java.util.Scanner};
     * 5) {@link Main}.
     *
     * @param args command-line arguments
     */
    public static void main(@NonNull String[] args) {
        String[] canonicalNames = args.length != 0 ? args : CANONICAL_NAMES;
        for (String canonicalName : canonicalNames) {
            Class<?> clazz;
            try {
                clazz = Class.forName(canonicalName);
            } catch (ClassNotFoundException e) {
                System.err.println("Specified class not found: " + canonicalName);
                continue;
            }

            String source;
            try {
                source = Reflector.printStructure(clazz);
            } catch (IOException e) {
                System.err.println("I/O error occurred: " + e.getMessage());
                continue;
            }

            try {
                Compiler.compileAndLoad(clazz.getSimpleName(), source);
            } catch (IOException e) {
                System.err.println("Output directory not found: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + e.getMessage());
            } catch (SecurityException e) {
                System.err.println("Security exception: " + e.getMessage());
            } catch (CompilationException e) {
                System.err.println("Compilation exception: " + e.getMessage());
            }
        }
    }
}
