package ru.spbau.kozlov.task04.compiler;

import checkers.nullness.quals.NonNull;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * @author adkozlov
 */
public class StringJavaSource extends SimpleJavaFileObject {

    @NonNull
    private final String code;

    public StringJavaSource(@NonNull String name, @NonNull String source) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = source;
    }

    @NonNull
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
