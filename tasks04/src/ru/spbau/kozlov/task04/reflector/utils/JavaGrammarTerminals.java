package ru.spbau.kozlov.task04.reflector.utils;

/**
 * The {@link JavaGrammarTerminals} class contains Java grammar terminals.
 *
 * @author adkozlov
 */
public final class JavaGrammarTerminals {

    /**
     * The packages delimiter character.
     */
    public static final char PACKAGE_DELIMITER = '.';
    /**
     * The nested class delimiter character.
     */
    public static final char NESTED_CLASS_DELIMITER = '.';
    /**
     * The inner class delimiter character.
     */
    public static final char INNER_CLASS_DELIMITER = '$';

    /**
     * The comma character.
     */
    public static final char COMMA = ',';
    /**
     * The semicolon character.
     */
    public static final char SEMICOLON = ';';
    /**
     * The assignment operation character.
     */
    public static final char ASSIGN = '=';

    /**
     * The left parenthesis character.
     */
    public static final char LEFT_PAREN = '(';
    /**
     * The right parenthesis character.
     */
    public static final char RIGHT_PAREN = ')';
    /**
     * The left brace character.
     */
    public static final char LEFT_BRACE = '{';
    /**
     * The right brace character.
     */
    public static final char RIGHT_BRACE = '}';
    /**
     * The left chevron character.
     */
    public static final char LEFT_CHEVRON = '<';
    /**
     * The right chevron character.
     */
    public static final char RIGHT_CHEVRON = '>';

    /**
     * The wild card type character.
     */
    public static final char WILD_CARD = '?';
    /**
     * The generic bounds delimiter character.
     */
    public static final char GENERIC_BOUNDS_DELIMITER = '&';

    /**
     * The {@code package} keyword.
     */
    public static final String PACKAGE_STRING = "package";
    /**
     * The {@code class} keyword.
     */
    public static final String CLASS_STRING = "class";
    /**
     * The {@code extends} keyword.
     */
    public static final String EXTENDS_STRING = "extends";
    /**
     * The {@code implements} keyword.
     */
    public static final String IMPLEMENTS_STRING = "implements";
    /**
     * The {@code throws} keyword.
     */
    public static final String THROWS_STRING = "throws";
    /**
     * The {@code return} keyword.
     */
    public static final String RETURN_STRING = "return";
    /**
     * The {@code super} keyword.
     */
    public static final String SUPER_STRING = "super";
    /**
     * The {@code default} keyword.
     */
    public static final String DEFAULT_STRING = "default";

    /**
     * The {@code null} keyword.
     */
    public static final String NULL_STRING = "null";
    /**
     * The zero string representation.
     */
    public static final String ZERO_STRING = "0";
    /**
     * The {@code false} keyword.
     */
    public static final String FALSE_STRING = "false";

    private JavaGrammarTerminals() {
    }
}
