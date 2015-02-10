package ru.spbau.kozlov.task01.messages.writers;

import org.jetbrains.annotations.NotNull;
import ru.spbau.kozlov.task01.messages.Message;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * The abstract class to write messages to the specified buffered writer.
 *
 * @author adkozlov
 * @see ru.spbau.kozlov.task01.messages.Message
 */
public abstract class AbstractMessageWriter extends AbstractResource implements MessageWriter {

    @NotNull
    private final BufferedWriter bufferedWriter;

    /**
     * Constructs a new writer with the specified output stream writer.
     *
     * @param outputStreamWriter an output stream writer where the messages are to be written to
     */
    public AbstractMessageWriter(@NotNull OutputStreamWriter outputStreamWriter) {
        bufferedWriter = new BufferedWriter(outputStreamWriter);
    }

    /**
     * Returns the buffered writer stored in the current writer.
     *
     * @return a print stream
     */
    @NotNull
    protected BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    @Override
    public void writeMessage(@NotNull Message message) throws IOException {
        try {
            writeMessageHeader(message);

            int i = 1;
            for (String line : message.getLines()) {
                writeMessageLine(line, i);
                i++;
            }
        } catch (IOException e) {
            IOErrorOccurred();
            throw e;
        }
    }

    /**
     * Writes the header of the specified message to the buffered writer.
     *
     * @param message a message which header is to be written
     * @throws IOException if an I/O error occurs.
     */
    protected void writeMessageHeader(@NotNull Message message) throws IOException {
        bufferedWriter.write(message.getSize() + "\n");
    }

    /**
     * Writes the specified line to the buffered writer.
     *
     * @param line  a line to be written
     * @param index an index of the line in the message
     * @throws IOException if an I/O error occurs.
     */
    protected void writeMessageLine(@NotNull String line, int index) throws IOException {
        bufferedWriter.write(line);
        bufferedWriter.newLine();
    }

    @Override
    public void flush() throws IOException {
        try {
            bufferedWriter.flush();
        } catch (IOException e) {
            IOErrorOccurred();
            throw e;
        }
    }

    /**
     * Closes this stream and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        try (BufferedWriter closeable = bufferedWriter) {
            if (!hasIOErrorOccurred()) {
                flush();
            }
        }
    }
}
