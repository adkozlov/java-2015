package ru.spbau.kozlov.task04.compiler.utils;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@link CompilerUtils} class contains util methods that are used by the {@link ru.spbau.kozlov.task04.compiler.Compiler} class.
 *
 * @author adkozlov
 */
public final class CompilerUtils {

    private static final Random RANDOM = new Random();

    private CompilerUtils() {
    }

    /**
     * Returns a new unique name for a class.
     *
     * @param name the old class name
     * @return the new class name
     */
    @NonNull
    public static String getUniqueName(@NonNull String name) {
        return name + "_" + Long.toHexString(RANDOM.nextLong());
    }

    /**
     * Returns the name of package by the source code.
     *
     * @param source the source code
     * @return the package name
     */
    public static String getPackageName(@NonNull String source) {
        Matcher matcher = Pattern.compile(JavaGrammarTerminals.PACKAGE_STRING + "\\s(.+?)" + JavaGrammarTerminals.SEMICOLON).
                matcher(source);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * Returns the canonical name of some class.
     *
     * @param simpleName  the simple name of the class
     * @param packageName the package name of the class
     * @return the canonical name
     */
    @NonNull
    public static String getCanonicalName(@NonNull String simpleName, @NonNull String packageName) {
        return (packageName != null ? packageName + JavaGrammarTerminals.PACKAGE_DELIMITER : "") + simpleName;
    }

    /**
     * Replaces all the occurrences of the class simple name in the source code.
     *
     * @param oldSimpleName the old simple name of the class
     * @param newSimpleName the new simple name of the class
     * @param source the source code
     * @return a new source code
     */
    public static String updateSource(@NonNull String oldSimpleName, @NonNull String newSimpleName, @NonNull String source) {
        return source.replaceAll("(\\s)" + oldSimpleName + "(\\W)", "$1" + newSimpleName + "$2");
    }
}
