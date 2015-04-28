package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaCodeStyleConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;
import ru.spbau.kozlov.task04.reflector.utils.ReflectorUtils;
import ru.spbau.kozlov.task04.reflector.utils.StringBuilderUtils;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The {@link ClassHeaderReflector} class analyses modifiers, superclass and implemented interfaces of the specified class.
 *
 * @author adkozlov
 */
public final class ClassHeaderReflector {

    private ClassHeaderReflector() {
    }

    /**
     * Creates a string representation of the specified class header.
     * Modifiers, superclass and implemented interfaces are included.
     * <p/>
     * Classes contained in the declared classes set are represented with their simple names.
     *
     * @param clazz           the specified class
     * @param indent          an indent to be used
     * @param declaredClasses a set of classes defined in the original class to be reflected
     * @return a string representation of the specified class header
     */
    @NonNull
    public static String createClassHeader(@NonNull Class<?> clazz, @NonNull String indent, @NonNull Set<@NonNull Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        builder.append(indent);
        boolean isInterface = clazz.isInterface();
        StringBuilderUtils.appendModifiersString(clazz.getModifiers(), builder);
        if (!isInterface) {
            builder.append(JavaGrammarTerminals.CLASS_STRING);
            builder.append(JavaCodeStyleConfig.SPACE);
        }
        builder.append(clazz.getSimpleName());

        builder.append(ReflectorUtils.createTypeParametersString(clazz.getTypeParameters(), declaredClasses));
        appendGenericSuperclass(clazz.getGenericSuperclass(), declaredClasses, builder);
        appendGenericInterfaces(isInterface, clazz.getGenericInterfaces(), declaredClasses, builder);

        return builder.toString();
    }

    /**
     * Analyzes the packages and all the modifiers of the two specified classes and returns the difference.
     *
     * @param first the first class to be analyzed
     * @param second the second class to be analyzed
     * @return a set of {@link Difference} class instances
     */
    @NonNull
    public static Set<Difference> diffClassHeaders(@NonNull Class<?> first, @NonNull Class<?> second) {
        Set<Difference> result = new LinkedHashSet<>();

        if (first.getModifiers() != second.getModifiers() ||
                !ReflectorUtils.checkGenericTypesAreEqual(first.getGenericSuperclass(), second.getGenericSuperclass()) ||
                !ReflectorUtils.checkGenericTypesAreEqual(first.getGenericInterfaces(), second.getGenericInterfaces())) {
            result.add(new Difference(first.toGenericString(), second.toGenericString(), "Class headers \'%s\' and \'%s' are not equal"));
        }

        return result;
    }

    private static void appendGenericSuperclass(Type superClassType, @NonNull Set<@NonNull Class<?>> declaredClasses, @NonNull StringBuilder result) {
        if (superClassType != null && superClassType != Object.class) {
            result.append(JavaCodeStyleConfig.SPACE);
            result.append(JavaGrammarTerminals.EXTENDS_STRING);
            result.append(JavaCodeStyleConfig.SPACE);
            result.append(ReflectorUtils.getTypeName(superClassType, declaredClasses));
        }
    }

    private static void appendGenericInterfaces(boolean isInterface, @NonNull Type[] interfacesTypes, @NonNull Set<@NonNull Class<?>> declaredClasses, @NonNull StringBuilder result) {
        if (interfacesTypes.length != 0) {
            result.append(JavaCodeStyleConfig.SPACE);
            result.append(isInterface ? JavaGrammarTerminals.EXTENDS_STRING : JavaGrammarTerminals.IMPLEMENTS_STRING);
            result.append(JavaCodeStyleConfig.SPACE);
            result.append(ReflectorUtils.createGenericTypesEnumerationString(interfacesTypes, String.valueOf(JavaGrammarTerminals.COMMA), declaredClasses));
        }
    }
}
