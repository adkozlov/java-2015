package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaFileConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;
import ru.spbau.kozlov.task04.reflector.utils.StringBuilderUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

/**
 * @author adkozlov
 */
public final class ConstructorReflector {

    private ConstructorReflector() {
    }

    public static String createConstructorString(@NonNull Constructor<?> constructor, @NonNull String indent, @NonNull Set<Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        builder.append(indent);
        StringBuilderUtils.appendModifiersString(constructor.getModifiers(), builder);

        Type[] genericParameterTypes = getGenericParameterTypes(constructor);
        builder.append(constructor.getDeclaringClass().getSimpleName());
        MethodReflector.appendArgumentsList(genericParameterTypes, declaredClasses, builder);
        MethodReflector.appendThrowsList(constructor.getGenericExceptionTypes(), declaredClasses, builder);

        builder.append(JavaFileConfig.SPACE);
        builder.append(JavaGrammarTerminals.LEFT_BRACE);
        builder.append(JavaFileConfig.NEW_LINE);
        appendSuperConstructorInvocation(constructor, genericParameterTypes.length, indent, builder);
        builder.append(indent);
        builder.append(JavaGrammarTerminals.RIGHT_BRACE);
        builder.append(JavaFileConfig.NEW_LINE);

        return builder.toString();
    }

    public static void appendSuperConstructorInvocation(@NonNull Constructor<?> constructor, int actualParameterCount, @NonNull String indent, @NonNull StringBuilder builder) {
        builder.append(indent);
        builder.append(JavaFileConfig.TAB);
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
                    builder.append(JavaFileConfig.createArgumentName(0));
                    for (int i = 1; i < parametersCount; i++) {
                        builder.append(JavaGrammarTerminals.COMMA);
                        builder.append(JavaFileConfig.SPACE);
                        builder.append(JavaFileConfig.createArgumentName(i));
                    }
                }
            }
        }

        builder.append(JavaGrammarTerminals.RIGHT_PAREN);
        builder.append(JavaGrammarTerminals.SEMICOLON);
        builder.append(JavaFileConfig.NEW_LINE);
    }

    private static boolean isInnerClassConstructor(@NonNull Constructor<?> constructor) {
        return isInnerClass(constructor.getDeclaringClass());
    }

    private static boolean isInnerClass(@NonNull Class<?> clazz) {
        return clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers());
    }

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
