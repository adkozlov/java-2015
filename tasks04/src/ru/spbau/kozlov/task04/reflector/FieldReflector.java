package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaFileConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;
import ru.spbau.kozlov.task04.reflector.utils.ReflectorUtils;
import ru.spbau.kozlov.task04.reflector.utils.StringBuilderUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * @author adkozlov
 */
public final class FieldReflector {

    private FieldReflector() {
    }

    public static String createFieldString(@NonNull Field field, @NonNull String indent, @NonNull Set<Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        int modifiers = field.getModifiers();
        builder.append(indent);
        StringBuilderUtils.appendModifiersString(modifiers, builder);

        builder.append(ReflectorUtils.getTypeName(field.getGenericType(), declaredClasses));
        builder.append(JavaFileConfig.SPACE);
        builder.append(field.getName());

        if (Modifier.isFinal(modifiers)) {
            builder.append(JavaFileConfig.SPACE);
            builder.append(JavaGrammarTerminals.ASSIGN);
            builder.append(JavaFileConfig.SPACE);
            builder.append(ReflectorUtils.createDefaultValueString(field.getType()));
        }

        builder.append(JavaGrammarTerminals.SEMICOLON);
        builder.append(JavaFileConfig.NEW_LINE);

        return builder.toString();
    }
}
