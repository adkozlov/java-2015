package ru.spbau.kozlov.task04.compiler.utils;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author adkozlov
 */
public final class CompilerUtils {

    private static final Random RANDOM = new Random();

    private CompilerUtils() {
    }

    @NonNull
    public static String getUniqueName(@NonNull String name) {
        return name + "_" + Long.toHexString(RANDOM.nextLong());
    }

    public static String getPackageName(@NonNull String source) {
        Matcher matcher = Pattern.compile(JavaGrammarTerminals.PACKAGE_STRING + "\\s(.+?)" + JavaGrammarTerminals.SEMICOLON).
                matcher(source);
        return matcher.find() ? matcher.group(1) : null;
    }

    @NonNull
    public static String getCanonicalName(@NonNull String simpleName, @NonNull String packageName) {
        return (packageName != null ? packageName + JavaGrammarTerminals.PACKAGE_DELIMITER : "") + simpleName;
    }

    public static String updateSource(@NonNull String oldSimpleName, @NonNull String newSimpleName, @NonNull String source) {
        return source.replaceAll("(\\s)" + oldSimpleName + "(\\W)", "$1" + newSimpleName + "$2");
    }
}
