package ru.spbau.kozlov.task02.zip.utils;

import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * The {@link ru.spbau.kozlov.task02.zip.utils.IOUtils} class contains useful methods for reading bytes from streams and writing to them.
 *
 * @author adkozlov
 */
public class IOUtils {

    /**
     * Reads all available bytes from the specified stream.
     *
     * @param inputStream the input stream to be read from
     * @return an array of bytes read from the stream
     * @throws IOException if an I/O error occurred during reading from the stream
     */
    @NotNull
    public static byte[] readContent(@NotNull InputStream inputStream) throws IOException {
        return readContent(inputStream, -1);
    }

    /**
     * Read specified number of bytes from the specified stream.
     *
     * @param inputStream the input stream to be read from
     * @param length      the number of bytes to read
     * @return an array of bytes read from the stream
     * @throws IOException if an I/O error occurred during reading from the stream
     */
    @NotNull
    public static byte[] readContent(@NotNull InputStream inputStream, long length) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            copy(bufferedInputStream, byteArrayOutputStream, length);
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * Writes the specified bytes array to the specified stream.
     *
     * @param outputStream the output stream to be written to
     * @param bytes        the bytes array to be written
     * @throws IOException if an I/O error occurred during writing to the stream
     */
    public static void writeContent(@NotNull OutputStream outputStream, @NotNull byte[] bytes) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            for (byte b : bytes) {
                bufferedOutputStream.write(b);
            }
        }
    }

    /**
     * Copies the specified number of bytes from one stream to another.
     *
     * @param inputStream  the input stream to be read from
     * @param outputStream the output stream to be written to
     * @param length       the number of bytes to be copied
     * @throws IOException if an I/O error occurred during reading or writing
     */
    public static void copy(@NotNull InputStream inputStream, @NotNull OutputStream outputStream, long length) throws IOException {
        if (length != -1) {
            for (long i = 0; i < length; i++) {
                copyByte(inputStream, outputStream);
            }
        } else {
            while (inputStream.available() > 0) {
                copyByte(inputStream, outputStream);
            }
        }
    }

    private static void copyByte(@NotNull InputStream inputStream, @NotNull OutputStream outputStream) throws IOException {
        outputStream.write(inputStream.read());
    }
}
