package ru.spbau.kozlov.task02.zip;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.kozlov.task02.zip.utils.IOUtils;
import ru.spbau.kozlov.task02.zip.utils.PathUtils;
import ru.spbau.kozlov.task02.zip.utils.ZipURLUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The {@link ru.spbau.kozlov.task02.zip.ZipCompressor} class implements zip-compressor.
 * Files, directories and web pages (specified with URL) are allowed.
 * If file or directory cannot be read, it is skipped. Invalid URLs are also skipped.
 *
 * @author adkozlov
 */
public class ZipCompressor extends ExceptionsContainer {

    @NotNull
    private final ZipOutputStream zipOutputStream;
    @NotNull
    private final DataOutputStream dataOutputStream;
    @NotNull
    private final List<URL> urls = new LinkedList<>();

    private boolean ioErrorOccurred = false;

    /**
     * Constructs a new compressor with the specified output archive file path.
     *
     * @param outputFilePath the path to the output archive
     * @throws IOException if an I/O error occurs during creating the archive file
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
     * @throws IOException if an I/O error occurs during writing to the archive file
     */
    public void putNextEntry(@NotNull String entry) throws IOException {
        if (!ZipURLUtils.isURL(entry)) {
            putNextEntry(Paths.get(entry));
        } else {
            try {
                urls.add(new URL(entry));
            } catch (MalformedURLException e) {
                addException(String.format("\'%s\' is not a valid URL", entry), e);
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
            writeDirEntry(ZipURLUtils.getUrlDirectoryName());
            for (URL url : urls) {
                putNextEntry(url);
            }

            if (!ioErrorOccurred) {
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            addContainedExceptionTo(e);
            throw e;
        }

        super.close();
    }

    private void putNextEntry(@NotNull Path path) throws IOException {
        final String pathString = path.toString();
        try {
            if (!Files.isReadable(path)) {
                addException(String.format("File \'%s\' cannot be read\n", pathString));
                return;
            }
        } catch (SecurityException e) {
            addException(String.format("File \'%s\' cannot be read because of security violation\n", pathString));
            return;
        }

        if (Files.isRegularFile(path)) {
            byte[] data = readFileContent(path);
            if (data != null) {
                writeEntry(pathString, data);
            }
        } else if (Files.isDirectory(path)) {
            putNextDirEntry(path);
        }
    }

    private void putNextEntry(@NotNull URL url) throws IOException {
        byte[] data = readURLContent(url);
        if (data != null) {
            writeEntry(url.toString(), data);
        }
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

            private boolean directoryIsEmpty(@NotNull Path dir) {
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
                    return !directoryStream.iterator().hasNext();
                } catch (IOException e) {
                    addException(e);
                }
                return true;
            }

            @Override
            public FileVisitResult visitFile(@NotNull Path file, @Nullable BasicFileAttributes attrs) throws IOException {
                putNextEntry(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(@NotNull Path file, @NotNull IOException e) {
                addException(e);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(@NotNull Path dir, @Nullable IOException e) {
                if (e != null) {
                    addException(e);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Nullable
    private byte[] readFileContent(@NotNull Path path) {
        try {
            return IOUtils.readContent(Files.newInputStream(path));
        } catch (IOException e) {
            addException(e);
        }
        return null;
    }

    @Nullable
    private byte[] readURLContent(@NotNull URL url) {
        try {
            return IOUtils.readContent(url.openConnection().getInputStream());
        } catch (IOException e) {
            addException(e);
        }
        return null;
    }

    private void writeEntry(@NotNull String path, long length, @Nullable byte[] content) throws IOException {
        if (!ioErrorOccurred) {
            try {
                dataOutputStream.writeUTF(ZipURLUtils.isURL(path) ? PathUtils.convertUrlToArchivePath(path) : PathUtils.convertOSPathToArchivePath(path));
                dataOutputStream.writeLong(length);
                if (content != null) {
                    dataOutputStream.write(content);
                }
            } catch (IOException e) {
                ioErrorOccurred = true;
                throw e;
            }
        }
    }

    private void writeEntry(@NotNull String path, @NotNull byte[] content) throws IOException {
        writeEntry(path, content.length, content);
    }

    private void writeDirEntry(@NotNull String path) throws IOException {
        writeEntry(path, -1, null);
    }
}
