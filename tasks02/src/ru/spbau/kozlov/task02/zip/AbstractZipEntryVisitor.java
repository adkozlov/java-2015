package ru.spbau.kozlov.task02.zip;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.kozlov.task02.zip.utils.IOUtils;
import ru.spbau.kozlov.task02.zip.utils.PathUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.zip.ZipInputStream;

/**
 * The {@link ru.spbau.kozlov.task02.zip.AbstractZipEntryVisitor} class implements a visitor that reads an zip-archieve and performs {@link ru.spbau.kozlov.task02.zip.AbstractZipEntryVisitor#onEntryVisit} action on every entry.
 *
 * @author adkozlov
 */
public abstract class AbstractZipEntryVisitor extends ExceptionsContainer {

    @NotNull
    private final ZipInputStream zipInputStream;
    @NotNull
    private final DataInputStream dataInputStream;

    /**
     * Constructs a new visitor with the specified input archive file path.
     *
     * @param inputFilePath the path to the input archive
     * @throws IOException if an I/O error occurs during opening the archive file
     * @throws SecurityException if the archive file cannot be read because of the security violation
     */
    public AbstractZipEntryVisitor(@NotNull Path inputFilePath) throws IOException, SecurityException {
        zipInputStream = new ZipInputStream(Files.newInputStream(inputFilePath));
        dataInputStream = new DataInputStream(new BufferedInputStream(zipInputStream));
    }

    /**
     * Closes this compressor and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        try {
            dataInputStream.close();
        } catch (IOException e) {
            addException(e);
        } finally {
            super.close();
        }
    }

    /**
     * Reads all entries contained in the archive file and performs an {@link AbstractZipEntryVisitor#onEntryVisit} action on every entry.
     *
     * @return a list of entries paths
     * @throws IOException if an I/O error occurs during reading the archive file
     */
    @NotNull
    protected LinkedList<String> visitAllEntries() throws IOException {
        zipInputStream.getNextEntry();

        LinkedList<String> result = new LinkedList<>();
        while (dataInputStream.available() > 0) {
            dataInputStream.mark(1);
            if (dataInputStream.read() == -1) {
                break;
            } else {
                dataInputStream.reset();
            }

            String path = dataInputStream.readUTF();
            long length = dataInputStream.readLong();
            byte[] content = length != -1 ? readEntryContent(length) : null;
            result.add(path);

            onEntryVisit(Paths.get(PathUtils.convertArchivePathToOSPath(path)), content);
        }

        return result;
    }

    /**
     * Reads the entry content from the archive file.
     *
     * @param length the number of bytes to be read
     * @return an array of bytes read from the archive file
     * @throws IOException if an I/O error occurred during the reading the archive file
     */
    @NotNull
    protected byte[] readEntryContent(long length) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(dataInputStream, byteArrayOutputStream, length);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Performs some action on the specified entry.
     *
     * @param path    a path of the specified entry
     * @param content an array of bytes containing the entry content
     */
    protected abstract void onEntryVisit(@NotNull Path path, @Nullable byte[] content);
}
