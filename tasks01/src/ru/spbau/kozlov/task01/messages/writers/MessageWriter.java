package ru.spbau.kozlov.task01.messages.writers;

import org.jetbrains.annotations.NotNull;
import ru.spbau.kozlov.task01.messages.Message;

import java.io.IOException;

/**
 * Interface for writing messages.
 *
 * @author adkozlov
 * @see ru.spbau.kozlov.task01.messages.Message
 */
public interface MessageWriter {

    /**
     * Writes the specified message.
     *
     * @param message a message to be written
     * @throws IOException if an I/O error occurs.
     */
    void writeMessage(@NotNull Message message) throws IOException;
}
