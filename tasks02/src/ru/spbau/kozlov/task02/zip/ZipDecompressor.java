package ru.spbau.kozlov.task02.zip;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipInputStream;

/**
 * The {@link ru.spbau.kozlov.task02.zip.ZipDecompressor} class implements zip-decompressor.
 *
 * @author adkozlov
 */
public class ZipDecompressor implements Closeable {

    @NotNull
    private final ZipInputStream zipInputStream;
    @NotNull
    private final DataInputStream dataInputStream;

    /**
     * Constructs a new decompressor.
     *
     * @param inputFilePath the path to the input archive
     * @throws IOException if an I/O error occurs
     */
    public ZipDecompressor(@NotNull Path inputFilePath) throws IOException {
        zipInputStream = new ZipInputStream(Files.newInputStream(inputFilePath));
        dataInputStream = new DataInputStream(new BufferedInputStream(zipInputStream));
    }

    /**
     * Extracts archive content to the current folder.
     *
     * @throws IOException if an I/O error occurs
     */
    public void extractAllEntries() throws IOException {
        getAllEntries(true);
    }

    /**
     * Prints a string representation of the file tree contained in the archive to the standard out.
     *
     * @throws IOException IOException if an I/O error occurs
     */
    public void listAllEntries() throws IOException {
        ArrayList<String> paths = getAllEntries(false);
        paths.add("");
        String[][] splitPaths = new String[paths.size()][];
        splitPaths[splitPaths.length - 1] = new String[0];

        for (int i = splitPaths.length - 2; i >= 0; i--) {
            splitPaths[i] = paths.get(i).split(File.separator);
            String[] current = splitPaths[i];
            boolean isUrl = current[0].equals(ZipCompressor.HTTP_PREFIX) && current.length != 1;

            for (int j = 0; j < current.length - 1; j++) {
                String prefix = !isUrl && j < splitPaths[i + 1].length ? "| " : "  ";
                current[j] = prefix + new String(new char[current[j].length()]).replaceAll("\0", " ");
            }
            current[current.length - 1] = "|_" + current[current.length - 1];
        }

        for (int i = 0; i < splitPaths.length - 1; i++) {
            for (String segment : splitPaths[i]) {
                System.out.print(segment);
            }
            System.out.println();
        }
    }

    /**
     * Closes this compressor and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        dataInputStream.close();
    }

    @NotNull
    private ArrayList<String> getAllEntries(boolean write) throws IOException {
        zipInputStream.getNextEntry();

        ArrayList<String> result = new ArrayList<>();
        while (dataInputStream.available() > 0) {
            dataInputStream.mark(1);
            if (dataInputStream.read() == -1) {
                break;
            } else {
                dataInputStream.reset();
            }

            String path = dataInputStream.readUTF();
            long length = dataInputStream.readLong();

            if (path.startsWith(ZipCompressor.HTTP_PREFIX)) {
                path = path.replaceFirst("/", File.separator);
            }

            if (write) {
                extractNextEntryContent(Paths.get(path), length);
                result.add(path);
            } else {
                skipNextEntryContent(length);
                if (!isHidden(path)) {
                    result.add(path);
                }
            }
        }

        return result;
    }

    private boolean isHidden(@NotNull String path) {
        return path.contains(File.separator + ".");
    }

    private void extractNextEntryContent(@NotNull Path path, long length) throws IOException {
        if (length != -1) {
            Path parentPath = path.getParent();
            if (parentPath != null && !Files.isWritable(parentPath)) {
                System.err.printf("Directory \'%s\' cannot be written to\n", parentPath.toString());
                skipNextEntryContent(length);
            } else {
                writeEntryContent(path, length);
            }
        } else {
            File dir = path.toFile();
            if (!dir.exists() && !dir.mkdirs()) {
                System.err.printf("Directory %s cannot be created\n", path.toString());
            }
        }
    }

    private void writeEntryContent(@NotNull Path path, long length) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(path))) {
            for (long i = 0; i < length; i++) {
                bufferedOutputStream.write(dataInputStream.read());
            }
        }
    }

    private void skipNextEntryContent(long length) throws IOException {
        dataInputStream.skipBytes((int) length);
    }
}
