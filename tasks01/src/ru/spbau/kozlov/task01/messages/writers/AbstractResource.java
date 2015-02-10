package ru.spbau.kozlov.task01.messages.writers;

import java.io.Closeable;
import java.io.Flushable;

/**
 * The abstract resource class describing some resource, that can be flushed and closed.
 * Also stores flag that some I/O error has occurred.
 *
 * @author adkozlov
 */
public abstract class AbstractResource implements Flushable, Closeable {

    private boolean ioErrorOccurred = false;

    /**
     * Constructs a new resource. The default value is that no I/O errors has occurred.
     */
    protected AbstractResource() {
    }

    /**
     * Sets true value to the flag.
     */
    protected void IOErrorOccurred() {
        ioErrorOccurred = true;
    }

    /**
     * Gets the flag value.
     *
     * @return true if some I/O error occurred
     */
    protected boolean hasIOErrorOccurred() {
        return ioErrorOccurred;
    }
}
