package ru.spbau.kozlov.task01.messages.writers;

import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;

/**
 * The {@link ru.spbau.kozlov.task01.messages.writers.FileMessageWriter} class is used to write messages to the specified file.
 *
 * @author adkozlov
 */
public class FileMessageWriter extends AbstractMessageWriter {

    /**
     * Constructs a new writer, given the name of a file to write to.
     *
     * @param fileName a name of the file to write to
     * @throws IOException if the named file cannot be opened to write.
     */
    public FileMessageWriter(@NotNull String fileName) throws IOException {
        super(new FileWriter(fileName));
    }
}
