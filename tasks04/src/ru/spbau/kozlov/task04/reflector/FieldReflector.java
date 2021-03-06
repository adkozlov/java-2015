package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaCodeStyleConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;
import ru.spbau.kozlov.task04.reflector.utils.ReflectorUtils;
import ru.spbau.kozlov.task04.reflector.utils.StringBuilderUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The {@link FieldReflector} class analyses modifiers and default values of the specified field.
 *
 * @author adkozlov
 */
public final class FieldReflector {

    private FieldReflector() {
    }

    /**
     * Creates a string representation of the specified field.
     * Modifiers and default value (only for final fields) are included.
     * <p/>
     * Classes contained in the declared classes set are represented with their simple names.
     *
     * @param field           the specified field
     * @param indent          an indent to be used
     * @param declaredClasses a set of classes defined in the original class to be reflected
     * @return a string representation of the specified field
     */
    @NonNull
    public static String createFieldString(@NonNull Field field, @NonNull String indent, @NonNull Set<@NonNull Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        int modifiers = field.getModifiers();
        builder.append(indent);
        StringBuilderUtils.appendModifiersString(modifiers, builder);

        builder.append(ReflectorUtils.getTypeName(field.getGenericType(), declaredClasses));
        builder.append(JavaCodeStyleConfig.SPACE);
        builder.append(field.getName());

        if (Modifier.isFinal(modifiers)) {
            builder.append(JavaCodeStyleConfig.SPACE);
            builder.append(JavaGrammarTerminals.ASSIGN);
            builder.append(JavaCodeStyleConfig.SPACE);
            builder.append(ReflectorUtils.createDefaultValueString(field.getType()));
        }

        builder.append(JavaGrammarTerminals.SEMICOLON);
        builder.append(JavaCodeStyleConfig.NEW_LINE);
        builder.append(JavaCodeStyleConfig.NEW_LINE);

        return builder.toString();
    }

    /**
     * Analyzes all the fields of the two specified classes and returns the difference.
     *
     * @param first the first class to be analyzed
     * @param second the second class to be analyzed
     * @return a set of {@link Difference} class instances
     */
    @NonNull
    public static Set<Difference> diffFields(@NonNull Class<?> first, @NonNull Class<?> second) {
        Set<Difference> result = new LinkedHashSet<>();

        for (Field firstClassField : first.getDeclaredFields()) {
            try {
                Field secondClassField = second.getDeclaredField(firstClassField.getName());
                if (firstClassField.getModifiers() != secondClassField.getModifiers() ||
                        !ReflectorUtils.checkGenericTypesAreEqual(firstClassField.getGenericType(), secondClassField.getGenericType())) {
                    result.add(new Difference(firstClassField.toGenericString(), secondClassField.toGenericString(), "Fields \'%s\' and \'%s\' are not equal"));
                }
            } catch (NoSuchFieldException e) {
                result.add(new Difference(second.getName(), firstClassField.toGenericString(), "Class \'%s\' has no field: \'%s\'"));
            }
        }

        return result;
    }
}
