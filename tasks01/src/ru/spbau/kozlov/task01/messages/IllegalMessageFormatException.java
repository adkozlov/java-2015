package ru.spbau.kozlov.task01.messages;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown to indicate that it is impossible to construct a valid {@link ru.spbau.kozlov.task01.messages.Message} instance with the specified input.
 *
 * @author adkozlov
 */
public class IllegalMessageFormatException extends Exception {

    private final int lineNumber;

    /**
     * Constructs a new exception with the specified detail message and number of line, where a format error occurred.
     *
     * @param message    the detail message
     * @param lineNumber the number of line
     */
    public IllegalMessageFormatException(@NotNull String message, int lineNumber) {
        super(message);
        this.lineNumber = lineNumber;
    }

    /**
     * Constructs a new exception with the specified detail message, cause and number of line where a format error occurred.
     *
     * @param message    the detail message
     * @param cause      the cause
     * @param lineNumber the number of line
     */
    public IllegalMessageFormatException(@NotNull String message, @NotNull Throwable cause, int lineNumber) {
        super(message, cause);
        this.lineNumber = lineNumber;
    }

    /**
     * Returns the number of line where a format error occurred.
     *
     * @return the number of line
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Returns a {@link String} object representing .
     *
     * @return a string representation of the exception
     */
    @Override
    public String toString() {
        return "IllegalMessageFormatException{" +
                "lineNumber=" + lineNumber +
                "} " + super.toString();
    }
}
