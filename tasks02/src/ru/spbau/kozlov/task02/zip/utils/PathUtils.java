package ru.spbau.kozlov.task02.zip.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * The {@link ru.spbau.kozlov.task02.zip.utils.PathUtils} class contains methods for converting URLs and filesystem paths to the path format used by {@link ru.spbau.kozlov.task02.zip.ZipCompressor} and {@link ru.spbau.kozlov.task02.zip.AbstractZipEntryVisitor} classes.
 * Filesystem paths could be OS dependent.
 *
 * @author adkozlov
 */
public final class PathUtils {

    public static final String ARCHIVE_FILE_SEPARATOR = "/";

    /**
     * Converts the specified filesystem path to an archive path.
     *
     * @param osPath the filesystem path string representation
     * @return an archive path string representation
     */
    public static String convertOSPathToArchivePath(@NotNull String osPath) {
        return osPath.replaceAll(File.separator, ARCHIVE_FILE_SEPARATOR);
    }

    /**
     * Converts the specified URL to an archive path.
     *
     * @param url the URL string representation
     * @return an archive path string representation
     */
    public static String convertUrlToArchivePath(@NotNull String url) {
        return url.replaceAll("/", "_").replaceFirst(":__", ARCHIVE_FILE_SEPARATOR);
    }

    /**
     * Converts the archive path to a filesystem path.
     *
     * @param archivePath the archive path string representation
     * @return an filesystem path
     */
    public static String convertArchivePathToOSPath(@NotNull String archivePath) {
        return archivePath.replaceAll(ARCHIVE_FILE_SEPARATOR, File.separator);
    }
}
