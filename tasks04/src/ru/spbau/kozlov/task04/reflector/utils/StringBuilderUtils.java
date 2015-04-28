package ru.spbau.kozlov.task04.reflector.utils;

import checkers.nullness.quals.NonNull;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.Consumer;

/**
 *
 *
 * @author adkozlov
 */
public final class StringBuilderUtils {

    private StringBuilderUtils() {
    }

    /**
     * @param modifiers
     * @param builder
     */
    public static void appendModifiersString(int modifiers, @NonNull StringBuilder builder) {
        String modifiersString = Modifier.toString(modifiers);
        if (!modifiersString.isEmpty()) {
            builder.append(modifiersString);
            builder.append(JavaCodeStyleConfig.SPACE);
        }
    }

    /**
     *
     * Classes contained in the declared classes set are represented with their simple names.
     *
     * @param argumentsTypes
     * @param declaredClasses a set of classes defined in the original class to be reflected
     * @param builder
     */
    public static void appendArgumentsList(@NonNull Type[] argumentsTypes, @NonNull Set<@NonNull Class<?>> declaredClasses, @NonNull StringBuilder builder) {
        int[] counter = new int[1];
        builder.append(JavaGrammarTerminals.LEFT_PAREN);
        StringBuilderUtils.appendTypesString(argumentsTypes,
                type -> {
                    builder.append(ReflectorUtils.getTypeName(type, declaredClasses));
                    builder.append(JavaCodeStyleConfig.SPACE);
                    builder.append(JavaCodeStyleConfig.createArgumentName(counter[0]));
                    counter[0]++;
                },
                builder);
        builder.append(JavaGrammarTerminals.RIGHT_PAREN);
    }

    /**
     *
     *
     * Classes contained in the declared classes set are represented with their simple names.
     *
     * @param exceptionsTypes
     * @param declaredClasses a set of classes defined in the original class to be reflected
     * @param builder
     */
    public static void appendThrowsList(@NonNull Type[] exceptionsTypes, @NonNull Set<@NonNull Class<?>> declaredClasses, @NonNull StringBuilder builder) {
        if (exceptionsTypes.length != 0) {
            builder.append(JavaCodeStyleConfig.SPACE);
            builder.append(JavaGrammarTerminals.THROWS_STRING);
            builder.append(JavaCodeStyleConfig.SPACE);
            StringBuilderUtils.appendTypesString(exceptionsTypes,
                    type -> builder.append(ReflectorUtils.getTypeName(type, declaredClasses)),
                    builder);
        }
    }

    /**
     * @param types
     * @param consumer
     * @param builder
     * @param <T>
     */
    public static <T extends Type> void appendTypeParametersString(@NonNull T[] types, @NonNull Consumer<T> consumer, @NonNull StringBuilder builder) {
        if (types.length != 0) {
            builder.append(JavaGrammarTerminals.LEFT_CHEVRON);
            appendTypesString(types, consumer, builder);
            builder.append(JavaGrammarTerminals.RIGHT_CHEVRON);
        }
    }

    /**
     *
     * @param types
     * @param consumer
     * @param builder
     * @param <T>
     */
    public static <T extends Type> void appendTypesString(@NonNull T[] types, @NonNull Consumer<T> consumer, @NonNull StringBuilder builder) {
        appendTypesString(types, Character.toString(JavaGrammarTerminals.COMMA), consumer, builder);
    }

    /**
     *
     * @param types
     * @param delimiter
     * @param consumer
     * @param builder
     * @param <T>
     */
    public static <T extends Type> void appendTypesString(@NonNull T[] types, @NonNull String delimiter, @NonNull Consumer<T> consumer, @NonNull StringBuilder builder) {
        if (types.length != 0) {
            consumer.accept(types[0]);
            for (int i = 1; i < types.length; i++) {
                builder.append(delimiter);
                builder.append(JavaCodeStyleConfig.SPACE);
                consumer.accept(types[i]);
            }
        }
    }

    /**
     *
     * Classes contained in the declared classes set are represented with their simple names.
     *
     * @param bounds
     * @param isUpperBound
     * @param declaredClasses a set of classes defined in the original class to be reflected
     * @param builder
     */
    public static void appendBoundsString(@NonNull Type[] bounds, boolean isUpperBound, @NonNull Set<@NonNull Class<?>> declaredClasses, @NonNull StringBuilder builder) {
        String typesString = ReflectorUtils.createGenericTypesEnumerationString(bounds, String.valueOf(JavaCodeStyleConfig.SPACE) + JavaGrammarTerminals.GENERIC_BOUNDS_DELIMITER, declaredClasses);
        if (!typesString.isEmpty()) {
            builder.append(JavaCodeStyleConfig.SPACE);
            builder.append(isUpperBound ? JavaGrammarTerminals.EXTENDS_STRING : JavaGrammarTerminals.SUPER_STRING);
            builder.append(JavaCodeStyleConfig.SPACE);
            builder.append(typesString);
        }
    }
}
