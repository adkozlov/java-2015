package ru.spbau.kozlov.task02.zip;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.kozlov.task02.zip.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The {@link ru.spbau.kozlov.task02.zip.ZipDecompressor} class implements zip-decompressor.
 * If extracted file cannot be placed to the file system, it is skipped. Web pages contained by the archive are extracted to the "http" directory.
 *
 * @author adkozlov
 */
public class ZipDecompressor extends AbstractZipEntryVisitor {
    /**
     * Constructs a new decompressor with the specified input archive file path.
     *
     * @param inputFilePath the path to the input archive
     * @throws java.io.IOException if an I/O error occurs during opening the archive file
     */
    public ZipDecompressor(@NotNull Path inputFilePath) throws IOException {
        super(inputFilePath);
    }

    /**
     * Extracts the archive content to the current folder.
     *
     * @throws IOException if an I/O error occurs during reading the archive file
     */
    public void extractAllEntries() throws IOException {
        visitAllEntries();
    }

    /**
     * Writes the entry content to the specified file.
     *
     * @param path a path of the specified entry
     * @param content an array of bytes containing the entry content
     */
    @Override
    protected void onEntryVisit(@NotNull Path path, @Nullable byte[] content) {
        writeEntryContent(path, content);
    }

    private void writeEntryContent(@NotNull Path path, @Nullable byte[] content) {
        if (content != null) {
            Path parentPath = path.getParent();
            try {
                if (parentPath != null && !Files.isWritable(parentPath)) {
                    addException(String.format("Directory \'%s\' cannot be written to\n", parentPath.toString()));
                    return;
                }
            } catch (SecurityException e) {
                addException(String.format("Directory \'%s\' writing permissions cannot be determined because of the security violation", path.toString()), e);
                return;
            }

            try {
                IOUtils.writeContent(Files.newOutputStream(path), content);
            } catch (IOException e) {
                addException(e);
            } catch (SecurityException e) {
                addException(String.format("File \'%s\' cannot be created because of the security violation", path.toString()), e);
            }
        } else {
            File dir = path.toFile();
            try {
                if (!dir.exists() && !dir.mkdirs()) {
                    addException(String.format("Directory \'%s\' cannot be created\n", path.toString()));
                }
            } catch (SecurityException e) {
                addException(String.format("Directory \'%s\' cannot be created because of the security violation\n", path.toString()), e);
            }
        }
    }
}
