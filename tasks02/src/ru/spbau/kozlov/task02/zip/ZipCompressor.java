package ru.spbau.kozlov.task02.zip;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The {@link ZipCompressor} class implements zip-compressor.
 * Files, directories and web pages (specified with URL) are allowed.
 *
 * @author adkozlov
 */
public class ZipCompressor implements Closeable {

    static final String HTTP_PREFIX = "http";
    private static final String URL_PREFIX = HTTP_PREFIX + "://";

    @NotNull
    private final ZipOutputStream zipOutputStream;
    @NotNull
    private final DataOutputStream dataOutputStream;
    @NotNull
    private final List<Path> paths = new LinkedList<>();
    @NotNull
    private final List<URL> urls = new LinkedList<>();

    private boolean errorOccurred = false;

    /**
     * Constructs a new compressor.
     *
     * @param outputFilePath the path to the output archive
     * @throws IOException if an I/O error occurs
     */
    public ZipCompressor(@NotNull Path outputFilePath) throws IOException {
        zipOutputStream = new ZipOutputStream(Files.newOutputStream(outputFilePath));
        dataOutputStream = new DataOutputStream(zipOutputStream);
        zipOutputStream.putNextEntry(new ZipEntry("root"));
    }

    /**
     * Puts the specified filesystem entry or web page to the archive.
     * Directories are added recursively, empty directories are ignored.
     *
     * @param entry the path to the specified entry or URL
     * @throws MalformedURLException if entry is not a valid URL
     */
    public void putNextEntry(@NotNull String entry) throws MalformedURLException {
        if (!entry.startsWith(URL_PREFIX)) {
            paths.add(Paths.get(entry));
        } else {
            try {
                urls.add(new URL(entry));
            } catch (MalformedURLException e) {
                errorOccurred = true;
                throw e;
            }
        }
    }

    /**
     * Closes this compressor and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        try (DataOutputStream ignored = dataOutputStream) {
            if (!errorOccurred) {
                for (Path path : paths) {
                    putNextEntry(path);
                }

                writeDirEntry(HTTP_PREFIX);
                for (URL url : urls) {
                    putNextEntry(url);
                }

                zipOutputStream.closeEntry();
            }
        }
    }

    private void putNextEntry(@NotNull Path path) throws IOException {
        final String pathString = path.toString();
        try {
            if (!Files.isReadable(path)) {
                System.err.printf("File %s cannot be read\n", pathString);
                return;
            }
        } catch (SecurityException e) {
            System.err.printf("File %s cannot be read because of security violation\n", pathString);
            return;
        }

        if (Files.isRegularFile(path)) {
            writeEntry(pathString, Files.size(path), readContent(Files.newInputStream(path)));
        } else if (Files.isDirectory(path)) {
            putNextDirEntry(path);
        }
    }

    private void putNextEntry(@NotNull URL url) throws IOException {
        byte[] data = readContent(url.openConnection().getInputStream());
        writeEntry(url.toString().replaceAll("/", "_").replaceFirst(":__", "/"), data.length, data);
    }

    private void putNextDirEntry(@NotNull Path path) throws IOException {
        Files.walkFileTree(path, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(@NotNull Path dir, @Nullable BasicFileAttributes attrs) throws IOException {
                if (directoryIsEmpty(dir)) {
                    return FileVisitResult.TERMINATE;
                } else {
                    writeDirEntry(dir.toString());
                    return FileVisitResult.CONTINUE;
                }
            }

            private boolean directoryIsEmpty(@NotNull Path dir) throws IOException {
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
                    return !directoryStream.iterator().hasNext();
                }
            }

            @Override
            public FileVisitResult visitFile(@NotNull Path file, @Nullable BasicFileAttributes attrs) throws IOException {
                putNextEntry(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(@NotNull Path file, @NotNull IOException e) throws IOException {
                System.err.printf("File %s cannot be read because of I/O error: %s\n", file.toString(), e.getMessage());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(@NotNull Path dir, @Nullable IOException e) throws IOException {
                if (e != null) {
                    System.err.printf("I/O error occurred: %s", e.getMessage());
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @NotNull
    private byte[] readContent(@NotNull InputStream inputStream) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            copy(bufferedInputStream, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

    private static void copy(@NotNull BufferedInputStream bufferedInputStream, @NotNull OutputStream outputStream) throws IOException {
        while (bufferedInputStream.available() > 0) {
            outputStream.write(bufferedInputStream.read());
        }
    }

    private void writeEntry(@NotNull String path, long length, @Nullable byte[] content) throws IOException {
        dataOutputStream.writeUTF(path);
        dataOutputStream.writeLong(length);
        if (content != null) {
            dataOutputStream.write(content);
        }
    }

    private void writeDirEntry(@NotNull String path) throws IOException {
        writeEntry(path, -1, null);
    }
}
