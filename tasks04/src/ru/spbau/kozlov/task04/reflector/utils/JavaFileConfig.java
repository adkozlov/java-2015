package ru.spbau.kozlov.task04.reflector.utils;

/**
 * @author adkozlov
 */
public final class JavaFileConfig {

    public static final char SPACE = ' ';
    public static final String TAB = "    ";
    public static final String NEW_LINE = System.lineSeparator();

    public static final String ARGUMENT_NAME_FORMAT = "arg%d";
    public static final String JAVA_FILE_EXTENSION = ".java";

    private JavaFileConfig() {
    }

    public static String createArgumentName(int index) {
        return String.format(JavaFileConfig.ARGUMENT_NAME_FORMAT, index);
    }
}
