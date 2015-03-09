package ru.spbau.kozlov.task02.zip.utils;

import org.jetbrains.annotations.NotNull;

/**
 * The {@link ru.spbau.kozlov.task02.zip.utils.ZipURLUtils} class contains methods to handle web pages during compression or decompression.
 * All the web pages should be placed in one directory.
 *
 * @author adkozlov
 */
public final class ZipURLUtils {

    private static final String HTTP_PREFIX = "http";
    private static final String URL_PREFIX = HTTP_PREFIX + "://";

    /**
     * Checks if the specified entry is a web page.
     *
     * @param string a string
     * @return {@code true} if the argument is a web page address
     */
    public static boolean isURL(@NotNull String string) {
        return string.startsWith(URL_PREFIX);
    }

    /**
     * Returns the name of the directory where all the web pages should be stored.
     *
     * @return the directory name
     */
    public static String getUrlDirectoryName() {
        return HTTP_PREFIX;
    }
}
