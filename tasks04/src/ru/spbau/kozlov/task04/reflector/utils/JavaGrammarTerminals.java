package ru.spbau.kozlov.task04.reflector.utils;

/**
 * @author adkozlov
 */
public final class JavaGrammarTerminals {

    public static final char PACKAGE_DELIMITER = '.';
    public static final char CLASS_DELIMITER = '.';
    public static final char INNER_CLASS_DELIMITER = '$';

    public static final char COMMA = ',';
    public static final char SEMICOLON = ';';
    public static final char ASSIGN = '=';

    public static final char LEFT_PAREN = '(';
    public static final char RIGHT_PAREN = ')';
    public static final char LEFT_BRACE = '{';
    public static final char RIGHT_BRACE = '}';
    public static final char LEFT_CHEVRON = '<';
    public static final char RIGHT_CHEVRON = '>';

    public static final char WILD_CARD = '?';
    public static final String GENERIC_BOUNDS_DELIMITER = " &";

    public static final String PACKAGE_STRING = "package";
    public static final String CLASS_STRING = "class";
    public static final String EXTENDS_STRING = "extends";
    public static final String IMPLEMENTS_STRING = "implements";
    public static final String THROWS_STRING = "throws";
    public static final String RETURN_STRING = "return";
    public static final String SUPER_STRING = "super";
    public static final String DEFAULT_STRING = "default";

    public static final String NULL_STRING = "null";
    public static final String ZERO_STRING = "0";
    public static final String FALSE_STRING = "false";

    private JavaGrammarTerminals() {
    }
}
