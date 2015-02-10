package ru.spbau.kozlov.task01.messages;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The {@link ru.spbau.kozlov.task01.messages.Message} class contains a list of lines.
 *
 * @author adkozlov
 */
public class Message {

    @NotNull
    private final List<String> lines;

    /**
     * Constructs a new message with the given list of lines.
     *
     * @param lines a list of lines
     */
    public Message(@NotNull List<String> lines) {
        this.lines = new ArrayList<>(lines);
    }

    /**
     * Constructs a new message with the given array of lines.
     *
     * @param lines an array of lines
     */
    public Message(@NotNull String... lines) {
        this(Arrays.asList(lines));
    }

    /**
     * Constructs a new message with the same list of lines as in the passed message.
     *
     * @param message message to be copied
     */
    public Message(@NotNull Message message) {
        this(message.lines);
    }

    /**
     * Returns the list of lines contained by the message.
     *
     * @return an unmodifiable copy of the list of lines
     */
    @NotNull
    public List<String> getLines() {
        return Collections.unmodifiableList(lines);
    }

    /**
     * Returns the number of lines contained by the message.
     *
     * @return the number of lines
     */
    public int getSize() {
        return lines.size();
    }

    /**
     * Appends all the lines contained by the passed message to the current message.
     *
     * @param message message to be copied
     */
    public void append(@NotNull Message message) {
        lines.addAll(message.lines);
    }

    /**
     * Returns a {@link String} object representing the list of lines contained by the message.
     *
     * @return a string representation of the message
     */
    @Override
    public String toString() {
        return "Message{" +
                "lines=" + lines +
                '}';
    }
}
