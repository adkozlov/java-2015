package ru.spbau.kozlov.task04.compiler;

import checkers.nullness.quals.NonNull;

/**
 * The {@link CompilationException} class represents an exception during compilation.
 *
 * @author adkozlov
 */
public class CompilationException extends Exception {

    /**
     * Constructs a new exception with the specified message.
     *
     * @param message the specified message
     */
    public CompilationException(@NonNull String message) {
        super("Compilation error: " + message);
    }
}
