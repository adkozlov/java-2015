package ru.spbau.kozlov.task02.zip;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.kozlov.task02.zip.utils.IOUtils;
import ru.spbau.kozlov.task02.zip.utils.PathUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.zip.ZipInputStream;

/**
 * The {@link ru.spbau.kozlov.task02.zip.ZipDecompressor} class implements zip-decompressor.
 * If extracted file cannot be placed to the file system, it is skipped. Web pages contained by the archive are extracted to the "http" directory.
 *
 * @author adkozlov
 */
public class ZipDecompressor extends ExceptionsContainer {

    @NotNull
    private final ZipInputStream zipInputStream;
    @NotNull
    private final DataInputStream dataInputStream;

    /**
     * Constructs a new decompressor with the specified input archive file path.
     *
     * @param inputFilePath the path to the input archive
     * @throws IOException if an I/O error occurs during opening the archive file
     */
    public ZipDecompressor(@NotNull Path inputFilePath) throws IOException {
        zipInputStream = new ZipInputStream(Files.newInputStream(inputFilePath));
        dataInputStream = new DataInputStream(new BufferedInputStream(zipInputStream));
    }

    /**
     * Extracts the archive content to the current folder.
     *
     * @throws IOException if an I/O error occurs during reading the archive file
     */
    public void extractAllEntries() throws IOException {
        readAllEntries(true);
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
            addContainedExceptionTo(e);
            throw e;
        }

        super.close();
    }

    /**
     * Reads all entries contained in the archive file and writes them to appropriate files if the argument is {@code true}.
     *
     * @param write {@code true} if read files should be created
     * @return a list of entries paths
     * @throws IOException if an I/O error occurs during reading the archive file
     */
    @NotNull
    protected LinkedList<String> readAllEntries(boolean write) throws IOException {
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

            if (write) {
                writeEntryContent(Paths.get(PathUtils.convertArchivePathToOSPath(path)), content);
            }
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

    private void writeEntryContent(@NotNull Path path, @Nullable byte[] content) {
        if (content != null) {
            Path parentPath = path.getParent();
            if (parentPath != null && !Files.isWritable(parentPath)) {
                addException(String.format("Directory \'%s\' cannot be written to\n", parentPath.toString()));
            } else {
                try {
                    IOUtils.writeContent(Files.newOutputStream(path), content);
                } catch (IOException e) {
                    addException(e);
                }
            }
        } else {
            File dir = path.toFile();
            if (!dir.exists() && !dir.mkdirs()) {
                addException(String.format("Directory \'%s\' cannot be created\n", path.toString()));
            }
        }
    }
}
