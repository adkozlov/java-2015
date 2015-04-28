package ru.spbau.kozlov.task04.compiler;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * The {@link StringJavaSource} class represents the source code of a class.
 *
 * @author adkozlov
 */
public class StringJavaSource extends SimpleJavaFileObject {

    @NonNull
    private final String code;

    /**
     * Constructs a new instance with the specified source code.
     *
     * @param name   the class name
     * @param source the class source code
     */
    public StringJavaSource(@NonNull String name, @NonNull String source) {
        super(URI.create("string:///" + name.replace(JavaGrammarTerminals.PACKAGE_DELIMITER, '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = source;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
