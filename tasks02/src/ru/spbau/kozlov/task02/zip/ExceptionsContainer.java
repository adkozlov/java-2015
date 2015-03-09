package ru.spbau.kozlov.task02.zip;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;

/**
 * The {@link ru.spbau.kozlov.task02.zip.ExceptionsContainer} class implements an abstract resource with an error storage.
 * If some I/O error occurs during working with this resource, the work could be continued without a break.
 * {@link java.io.IOException} that had occurred would be saved in the storage and thrown after the work with the resource is done.
 *
 * @author adkozlov
 */
public abstract class ExceptionsContainer implements Closeable {

    @Nullable
    private IOException containedException;

    /**
     * Closes the resource and throws the first exception contained in the storage.
     * Other exceptions will be added as suppressed. If no error had occurred, no exception will be thrown.
     *
     * @throws IOException if container contains I/O errors
     */
    @Override
    public void close() throws IOException {
        if (containedException != null) {
            throw containedException;
        }
    }

    /**
     * Saves the specified I/O error in the storage.
     *
     * @param exception an exception to be saved
     */
    protected void addException(@NotNull IOException exception) {
        if (containedException != null) {
            containedException.addSuppressed(exception);
        } else {
            containedException = exception;
        }
    }

    /**
     * Constructs a new {@link java.io.IOException} with specified message and cause and saves it in the storage.
     *
     * @param message an error message
     * @param cause a cause of the error
     */
    protected void addException(@NotNull String message, @NotNull Throwable cause) {
        addException(new IOException(message, cause));
    }

    /**
     * Constructs a new {@link java.io.IOException} with specified message and saves it in the storage.
     *
     * @param message an error message
     */
    protected void addException(@NotNull String message) {
        addException(new IOException(message));
    }
}
