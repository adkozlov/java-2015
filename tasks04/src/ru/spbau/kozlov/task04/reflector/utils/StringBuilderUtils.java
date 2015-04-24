package ru.spbau.kozlov.task04.reflector.utils;

import checkers.nullness.quals.NonNull;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author adkozlov
 */
public final class StringBuilderUtils {

    private StringBuilderUtils() {
    }

    public static void appendModifiersString(int modifiers, @NonNull StringBuilder builder) {
        String modifiersString = Modifier.toString(modifiers);
        if (!modifiersString.isEmpty()) {
            builder.append(modifiersString);
            builder.append(JavaFileConfig.SPACE);
        }
    }

    public static <T extends Type> void appendTypeParametersString(@NonNull T[] types, @NonNull Consumer<T> consumer, @NonNull StringBuilder builder) {
        if (types.length != 0) {
            builder.append(JavaGrammarTerminals.LEFT_CHEVRON);
            appendTypesString(types, consumer, builder);
            builder.append(JavaGrammarTerminals.RIGHT_CHEVRON);
        }
    }

    public static <T extends Type> void appendTypesString(@NonNull T[] types, @NonNull Consumer<T> consumer, @NonNull StringBuilder builder) {
        appendTypesString(types, Character.toString(JavaGrammarTerminals.COMMA), consumer, builder);
    }

    public static <T extends Type> void appendTypesString(@NonNull T[] types, @NonNull String delimiter, @NonNull Consumer<T> consumer, @NonNull StringBuilder builder) {
        if (types.length != 0) {
            consumer.accept(types[0]);
            for (int i = 1; i < types.length; i++) {
                builder.append(delimiter);
                builder.append(JavaFileConfig.SPACE);
                consumer.accept(types[i]);
            }
        }
    }

    public static void appendBoundsString(@NonNull Type[] bounds, boolean isUpperBound, @NonNull Set<Class<?>> declaredClasses, @NonNull StringBuilder builder) {
        String typesString = ReflectorUtils.createGenericTypesEnumerationString(bounds, JavaGrammarTerminals.GENERIC_BOUNDS_DELIMITER, declaredClasses);
        if (!typesString.isEmpty()) {
            builder.append(JavaFileConfig.SPACE);
            builder.append(isUpperBound ? JavaGrammarTerminals.EXTENDS_STRING : JavaGrammarTerminals.SUPER_STRING);
            builder.append(JavaFileConfig.SPACE);
            builder.append(typesString);
        }
    }
}
