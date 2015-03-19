package ru.spbau.kozlov.task02.zip;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.kozlov.task02.zip.utils.FilterReversedIterator;
import ru.spbau.kozlov.task02.zip.utils.PathUtils;
import ru.spbau.kozlov.task02.zip.utils.ZipURLUtils;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The {@link ru.spbau.kozlov.task02.zip.ZipLister} class implements a tool that can list entries contained in the archive.
 * Files and directories would not be created during reading the archive file.
 *
 * @author adkozlov
 */
public class ZipLister extends AbstractZipEntryVisitor {
    /**
     * Constructs a new lister with the specified input archive file path.
     *
     * @param inputFilePath the path to the input archive
     * @throws IOException if an I/O error occurs
     */
    public ZipLister(@NotNull Path inputFilePath) throws IOException {
        super(inputFilePath);
    }

    /**
     * Returns a string representation of the file tree contained in the archive.
     *
     * @return a tree string representation
     * @throws IOException if an I/O error occurs during reading the archive file
     */
    @NotNull
    public String listAllEntries() throws IOException {
        String[] next = new String[0];
        String[] nextPrefixes = new String[0];

        StringBuilder stringBuilder = new StringBuilder();
        for (FilterReversedIterator iterator = new FilterReversedIterator(visitAllEntries()); iterator.hasNext(); ) {
            String[] current = iterator.next().split(PathUtils.ARCHIVE_FILE_SEPARATOR_STRING);
            String[] prefixes = new String[current.length];
            boolean isUrl = current[0].equals(ZipURLUtils.getUrlDirectoryName()) && current.length != 1;

            stringBuilder.insert(0, System.lineSeparator());
            stringBuilder.insert(0, current[current.length - 1]);
            stringBuilder.insert(0, "|_");
            prefixes[current.length - 1] = "| ";

            for (int j = current.length - 2; j >= 0; j--) {
                stringBuilder.insert(0, String.format("%" + current[j].length() + "s", " "));
                String prefix = !isUrl && j < next.length ? "| " : "  ";
                if (j < nextPrefixes.length && nextPrefixes[j] != null && nextPrefixes[j].equals("  ")) {
                    prefix = "  ";
                }
                stringBuilder.insert(0, prefix);
                prefixes[j] = prefix;
            }

            next = current;
            nextPrefixes = prefixes;
        }

        return stringBuilder.toString();
    }

    /**
     * Does nothing.
     *
     * @param path    a path of the specified entry
     * @param content an array of bytes containing the entry content
     */
    @Override
    protected void onEntryVisit(@NotNull Path path, @Nullable byte[] content) {
    }
}
