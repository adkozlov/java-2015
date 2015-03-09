package ru.spbau.kozlov.task02.zip;

import org.jetbrains.annotations.NotNull;
import ru.spbau.kozlov.task02.zip.utils.PathUtils;
import ru.spbau.kozlov.task02.zip.utils.ZipURLUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * The {@link ru.spbau.kozlov.task02.zip.ZipLister} class implements a tool that can list entries contained in the archive.
 * Files and directories would not be created during reading the archive file.
 *
 * @author adkozlov
 */
public class ZipLister extends ZipDecompressor {
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
    public String listAllEntries() throws IOException {
        LinkedList<String> paths = filterHiddenFiles(readAllEntries(false));

        String[] next = new String[0];
        StringBuilder stringBuilder = new StringBuilder();
        for (ListIterator<String> iterator = paths.listIterator(paths.size()); iterator.hasPrevious(); ) {
            String[] current = iterator.previous().split(PathUtils.ARCHIVE_FILE_SEPARATOR);
            boolean isUrl = current[0].equals(ZipURLUtils.getUrlDirectoryName()) && current.length != 1;

            stringBuilder.insert(0, "\n");
            stringBuilder.insert(0, current[current.length - 1]);
            stringBuilder.insert(0, "|_");

            for (int j = current.length - 2; j >= 0; j--) {
                stringBuilder.insert(0, new String(new char[current[j].length()]).replaceAll("\0", " "));
                stringBuilder.insert(0, !isUrl && j < next.length ? "| " : "  ");
            }

            next = current;
        }

        return stringBuilder.toString();
    }

    private static LinkedList<String> filterHiddenFiles(@NotNull LinkedList<String> paths) {
        for (Iterator<String> iterator = paths.iterator(); iterator.hasNext(); ) {
            if (fileIsHidden(iterator.next())) {
                iterator.remove();
            }
        }
        return paths;
    }

    private static boolean fileIsHidden(@NotNull String path) {
        return path.contains(PathUtils.ARCHIVE_FILE_SEPARATOR + ".");
    }
}
