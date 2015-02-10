package ru.spbau.kozlov.task01.messages.writers;

import org.jetbrains.annotations.NotNull;
import ru.spbau.kozlov.task01.messages.Message;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * The {@link ru.spbau.kozlov.task01.messages.writers.ConsoleMessageWriter} class if used to write messages to standard output.
 *
 * @author adkozlov
 * @see java.lang.System#out
 */
public class ConsoleMessageWriter extends AbstractMessageWriter {

    private int messagesWritten = 0;

    /**
     * Constructs a new standard output writer.
     */
    public ConsoleMessageWriter() {
        super(new OutputStreamWriter(System.out));
    }

    /**
     * Writes the count of lines in the specified message to the standard output.
     *
     * @param message a message which header is to be written
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void writeMessageHeader(@NotNull Message message) throws IOException {
        messagesWritten++;
        getBufferedWriter().write(String.format("Message %d\n", messagesWritten));
    }

    /**
     * Writes the specified line to the standard output.
     *
     * @param line  a line to be written
     * @param index an index of the line in the message
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void writeMessageLine(@NotNull String line, int index) throws IOException {
        getBufferedWriter().write(String.format("%d.%d. %s\n", messagesWritten, index, line));
    }

    @Override
    public void close() throws IOException {
        if (!hasIOErrorOccurred()) {
            flush();
        }
    }
}
