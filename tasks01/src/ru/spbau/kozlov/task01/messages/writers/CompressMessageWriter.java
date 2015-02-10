package ru.spbau.kozlov.task01.messages.writers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.kozlov.task01.messages.Message;

import java.io.IOException;

/**
 * The {@link ru.spbau.kozlov.task01.messages.writers.CompressMessageWriter} class is used to zip every two messages into one an write it with the specified {@link ru.spbau.kozlov.task01.messages.writers.MessageWriter}.
 *
 * @author adkozlov
 */
public class CompressMessageWriter extends AbstractResource implements MessageWriter {

    @NotNull
    private final AbstractMessageWriter messageWriter;
    @Nullable
    private Message buffer = null;

    /**
     * Constructs a new writer that uses the specified writer to write messages.
     *
     * @param messageWriter a writer to be used to write messages
     */
    public CompressMessageWriter(@NotNull AbstractMessageWriter messageWriter) {
        this.messageWriter = messageWriter;
    }

    /**
     * Zips every two messages into one and writes a new message.
     * If the last message has an odd number, than it is to be written as is.
     *
     * @param message a message to be written
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void writeMessage(@NotNull Message message) throws IOException {
        if (buffer != null) {
            buffer.append(message);

            try {
                messageWriter.writeMessage(buffer);
            } catch (IOException e) {
                IOErrorOccurred();
                throw e;
            }

            buffer = null;
        } else {
            buffer = new Message(message);
        }
    }

    @Override
    public void flush() throws IOException {
        if (buffer != null) {
            try {
                messageWriter.writeMessage(buffer);
                buffer = null;
            } catch (IOException e) {
                IOErrorOccurred();
                throw e;
            }
        }
    }

    /**
     * Closes this stream and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        try (AbstractMessageWriter closeable = messageWriter) {
            if (!hasIOErrorOccurred()) {
                flush();
            }
        }
    }
}
