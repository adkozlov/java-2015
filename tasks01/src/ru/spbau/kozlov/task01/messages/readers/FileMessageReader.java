package ru.spbau.kozlov.task01.messages.readers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.kozlov.task01.messages.IllegalMessageFormatException;
import ru.spbau.kozlov.task01.messages.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link ru.spbau.kozlov.task01.messages.readers.FileMessageReader} class is used to read number of messages from the specified file one by one.
 *
 * @author adkozlov
 * @see ru.spbau.kozlov.task01.messages.Message
 */
public class FileMessageReader implements Closeable {

    @NotNull
    private final BufferedReader bufferedReader;
    private int linesRead = 0;

    /**
     * Constructs a new reader, given the name of a file to read from.
     *
     * @param fileName a name of the file to read from
     * @throws FileNotFoundException if the named file does not exists.
     */
    public FileMessageReader(@NotNull String fileName) throws FileNotFoundException {
        bufferedReader = new BufferedReader(new FileReader(fileName));
    }

    /**
     * Reads next message from the file.
     *
     * @return a {@link ru.spbau.kozlov.task01.messages.Message} class object containing message that was read, or null if the end of file has been reached
     * @throws IOException                   if an I/O error occurs.
     * @throws IllegalMessageFormatException if no valid message can be created with the given input.
     */
    @Nullable
    public Message readMessage() throws IOException, IllegalMessageFormatException {
        int size = readSize();
        if (size == -1) {
            return null;
        }

        return new Message(readBody(size));
    }

    @Nullable
    private String readLine() throws IOException {
        linesRead++;
        return bufferedReader.readLine();
    }

    private int readSize() throws IOException, IllegalMessageFormatException {
        String line = readLine();
        if (line == null) {
            return -1;
        }

        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            throw new IllegalMessageFormatException(
                    "Message lines count should be an integer number (actual line: \"" + line + "\")", e, linesRead);
        }
    }

    @NotNull
    private List<String> readBody(int size) throws IOException, IllegalMessageFormatException {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String line = readLine();
            if (line != null) {
                result.add(line);
            } else {
                throw new IllegalMessageFormatException(
                        "Not enough message lines (" + i + " of " + size + ")", linesRead);
            }
        }

        return result;
    }

    /**
     * Closes this stream and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        bufferedReader.close();
    }
}
