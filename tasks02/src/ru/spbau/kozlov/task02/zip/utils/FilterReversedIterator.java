package ru.spbau.kozlov.task02.zip.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * The {@link ru.spbau.kozlov.task02.zip.utils.FilterReversedIterator} class implements a reversed iterator that filters hidden files paths from the list.
 *
 * @author adkozlov
 */
public class FilterReversedIterator implements Iterator<String> {

    @NotNull
    private final ListIterator<String> iterator;

    /**
     * Constructs a new iterator over the specified list.
     *
     * @param list a list of file paths
     */
    public FilterReversedIterator(@NotNull List<String> list) {
        iterator = list.listIterator(list.size());
    }

    /**
     * Checks if the iteration has paths that represent not hidden files
     *
     * @return {@code true} if the iteration has at least one more path that represent a not hidden file
     */
    @Override
    public boolean hasNext() {
        while (iterator.hasPrevious()) {
            if (!fileIsHidden(iterator.previous())) {
                iterator.next();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the next path in the iteration that represents a not hidden file
     *
     * @return the next path representing a not hidden file
     * @throws java.util.NoSuchElementException if the iteration has no more paths or all of them represents hidden files
     */
    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more files left or all of them are hidden");
        }
        return iterator.previous();
    }

    private static boolean fileIsHidden(@NotNull String path) {
        return path.contains(PathUtils.ARCHIVE_FILE_SEPARATOR + ".");
    }
}
