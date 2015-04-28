package ru.spbau.kozlov.task04.reflector.utils;

import checkers.nullness.quals.NonNull;

/**
 * The {@link JavaCodeStyleConfig} class contains
 *
 * @author adkozlov
 */
public final class JavaCodeStyleConfig {

    /**
     * The space character.
     */
    public static final char SPACE = ' ';
    /**
     * The tabulation string.
     */
    public static final String TAB = "    ";
    /**
     * The OS-dependent new line string.
     */
    public static final String NEW_LINE = System.lineSeparator();

    private static final String ARGUMENT_NAME_FORMAT = "arg%d";

    private JavaCodeStyleConfig() {
    }

    /**
     * Returns a name of the argument based on its index.
     *
     * @param index an argument index
     * @return the argument name
     */
    @NonNull
    public static String createArgumentName(int index) {
        return String.format(JavaCodeStyleConfig.ARGUMENT_NAME_FORMAT, index);
    }
}
