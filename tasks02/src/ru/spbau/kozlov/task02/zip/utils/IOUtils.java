package ru.spbau.kozlov.task02.zip.utils;

import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * The {@link ru.spbau.kozlov.task02.zip.utils.IOUtils} class contains useful methods for reading bytes from streams and writing to them.
 *
 * @author adkozlov
 */
public final class IOUtils {

    private static final int BUFFER_SIZE = 4096;

    /**
     * Reads all available bytes from the specified stream.
     *
     * @param inputStream the input stream to be read from
     * @return an array of bytes read from the stream
     * @throws IOException if an I/O error occurred during reading from the stream
     */
    @NotNull
    public static byte[] readContent(@NotNull InputStream inputStream) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (inputStream.available() > 0) {
                byteArrayOutputStream.write(bufferedInputStream.read());
            }
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
            bufferedOutputStream.write(bytes);
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
        byte[] buffer = new byte[BUFFER_SIZE];
        while (length > buffer.length) {
            length -= copy(inputStream, outputStream, buffer, buffer.length);
        }
        while (length != 0) {
            length -= copy(inputStream, outputStream, buffer, (int) length);
        }
    }

    private static int copy(@NotNull InputStream inputStream, @NotNull OutputStream outputStream, @NotNull byte[] buffer, int length) throws IOException {
        int bytesRead = inputStream.read(buffer, 0, length);
        if (bytesRead == -1) {
            throw new IOException("Not enough bytes");
        }
        outputStream.write(buffer, 0, bytesRead);
        return bytesRead;
    }
}
