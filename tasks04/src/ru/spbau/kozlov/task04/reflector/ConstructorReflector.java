package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaCodeStyleConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;
import ru.spbau.kozlov.task04.reflector.utils.ReflectorUtils;
import ru.spbau.kozlov.task04.reflector.utils.StringBuilderUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The {@link ConstructorReflector} class analyses modifiers and the list of exceptions that can be thrown of the specified constructor.
 *
 * @author adkozlov
 */
public final class ConstructorReflector {

    private ConstructorReflector() {
    }

    /**
     * Creates a string representation of the specified constructor.
     * Modifiers, list of exceptions that can be thrown are included.
     * The generated constructor calls the constructor of the superclass.
     * <p/>
     * Classes contained in the declared classes set are represented with their simple names.
     *
     * @param constructor     the specified constructor
     * @param indent          an indent to be used
     * @param declaredClasses a set of classes defined in the original class to be reflected
     * @return a string representation of the specified constructor
     */
    @NonNull
    public static String createConstructorString(@NonNull Constructor<?> constructor, @NonNull String indent, @NonNull Set<@NonNull Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        builder.append(indent);
        StringBuilderUtils.appendModifiersString(constructor.getModifiers(), builder);

        Type[] genericParameterTypes = getGenericParameterTypes(constructor);
        builder.append(constructor.getDeclaringClass().getSimpleName());
        StringBuilderUtils.appendArgumentsList(genericParameterTypes, declaredClasses, builder);
        StringBuilderUtils.appendThrowsList(constructor.getGenericExceptionTypes(), declaredClasses, builder);

        builder.append(JavaCodeStyleConfig.SPACE);
        builder.append(JavaGrammarTerminals.LEFT_BRACE);
        builder.append(JavaCodeStyleConfig.NEW_LINE);
        appendSuperConstructorInvocation(constructor, genericParameterTypes.length, indent, builder);
        builder.append(indent);
        builder.append(JavaGrammarTerminals.RIGHT_BRACE);
        builder.append(JavaCodeStyleConfig.NEW_LINE);
        builder.append(JavaCodeStyleConfig.NEW_LINE);

        return builder.toString();
    }

    private static void appendSuperConstructorInvocation(@NonNull Constructor<?> constructor, int actualParameterCount, @NonNull String indent, @NonNull StringBuilder builder) {
        builder.append(indent);
        builder.append(JavaCodeStyleConfig.TAB);
        builder.append(JavaGrammarTerminals.SUPER_STRING);
        builder.append(JavaGrammarTerminals.LEFT_PAREN);

        Class<?> superClass = constructor.getDeclaringClass().getSuperclass();
        if (superClass != null && superClass != Object.class) {
            Constructor<?>[] superClassConstructors = superClass.getDeclaredConstructors();
            if (superClassConstructors.length != 0) {
                int minParameterCount = Integer.MAX_VALUE;
                for (Constructor<?> superClassConstructor : superClassConstructors) {
                    minParameterCount = Math.min(superClassConstructor.getParameterCount(), minParameterCount);
                }

                int parametersCount = Math.min(actualParameterCount, minParameterCount - getHiddenParameterCount(superClass));
                if (parametersCount != 0) {
                    builder.append(JavaCodeStyleConfig.createArgumentName(0));
                    for (int i = 1; i < parametersCount; i++) {
                        builder.append(JavaGrammarTerminals.COMMA);
                        builder.append(JavaCodeStyleConfig.SPACE);
                        builder.append(JavaCodeStyleConfig.createArgumentName(i));
                    }
                }
            }
        }

        builder.append(JavaGrammarTerminals.RIGHT_PAREN);
        builder.append(JavaGrammarTerminals.SEMICOLON);
        builder.append(JavaCodeStyleConfig.NEW_LINE);
    }

    /**
     * Analyzes all the constructors of the two specified classes and returns the difference.
     *
     * @param first the first class to be analyzed
     * @param second the second class to be analyzed
     * @return a set of {@link Difference} class instances
     */
    @NonNull
    public static Set<Difference> diffConstructors(@NonNull Class<?> first, @NonNull Class<?> second) {
        Set<Difference> result = new LinkedHashSet<>();

        for (Constructor<?> firstClassConstructor : first.getDeclaredConstructors()) {
            try {
                Constructor<?> secondClassConstructor = second.getDeclaredConstructor(firstClassConstructor.getParameterTypes());
                if (firstClassConstructor.getModifiers() != secondClassConstructor.getModifiers() ||
                        !ReflectorUtils.checkGenericTypesAreEqual(firstClassConstructor.getGenericParameterTypes(), secondClassConstructor.getGenericParameterTypes()) ||
                        !ReflectorUtils.checkGenericTypesAreEqual(firstClassConstructor.getGenericExceptionTypes(), secondClassConstructor.getGenericExceptionTypes())) {
                    result.add(new Difference(firstClassConstructor.toGenericString(), secondClassConstructor.toGenericString(), "Constructors \'%s\' and \'%s\' are not equal"));
                }
            } catch (NoSuchMethodException e) {
                result.add(new Difference(second.getName(), firstClassConstructor.toGenericString(), "Class \'%s\' has no constructor: \'%s\'"));
            }
        }

        return result;
    }

    private static boolean isInnerClassConstructor(@NonNull Constructor<?> constructor) {
        return isInnerClass(constructor.getDeclaringClass());
    }

    private static boolean isInnerClass(@NonNull Class<?> clazz) {
        return clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers());
    }

    @NonNull
    private static Type[] getGenericParameterTypes(@NonNull Constructor<?> constructor) {
        Type[] result = constructor.getGenericParameterTypes();
        return !isInnerClassConstructor(constructor) ? result : Arrays.copyOfRange(result, 1, result.length);
    }

    private static int getHiddenParameterCount(@NonNull Class<?> clazz) {
        int result = 0;
        for (Class<?> declaringClass = clazz; isInnerClass(declaringClass); declaringClass = declaringClass.getDeclaringClass()) {
            result++;
        }
        return result;
    }
}
