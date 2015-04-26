package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaFileConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;
import ru.spbau.kozlov.task04.reflector.utils.ReflectorUtils;
import ru.spbau.kozlov.task04.reflector.utils.StringBuilderUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author adkozlov
 */
public final class MethodReflector {

    private MethodReflector() {
    }

    @NonNull
    public static String createMethodString(@NonNull Method method, @NonNull String indent, @NonNull Set<@NonNull Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        builder.append(indent);
        appendModifiers(method, builder);

        String typeParameters = ReflectorUtils.createTypeParametersString(method.getTypeParameters(), declaredClasses);
        if (!typeParameters.isEmpty()) {
            builder.append(typeParameters);
            builder.append(JavaFileConfig.SPACE);
        }
        builder.append(ReflectorUtils.getTypeName(method.getGenericReturnType(), declaredClasses));
        builder.append(JavaFileConfig.SPACE);
        builder.append(method.getName());

        appendArgumentsList(method.getGenericParameterTypes(), declaredClasses, builder);
        appendThrowsList(method.getGenericExceptionTypes(), declaredClasses, builder);
        appendMethodBody(method, indent, builder);
        builder.append(JavaFileConfig.NEW_LINE);
        builder.append(JavaFileConfig.NEW_LINE);

        return builder.toString();
    }

    private static void appendModifiers(@NonNull Method method, @NonNull StringBuilder builder) {
        int modifiers = method.getModifiers();
        if (method.getDeclaringClass().isInterface() && !Modifier.isAbstract(modifiers)) {
            builder.append(JavaGrammarTerminals.DEFAULT_STRING);
            builder.append(JavaFileConfig.SPACE);
        } else {
            StringBuilderUtils.appendModifiersString(modifiers, builder);
        }
    }

    static void appendArgumentsList(@NonNull Type[] argumentsTypes, @NonNull Set<@NonNull Class<?>> declaredClasses, @NonNull StringBuilder builder) {
        int[] counter = new int[1];
        builder.append(JavaGrammarTerminals.LEFT_PAREN);
        StringBuilderUtils.appendTypesString(argumentsTypes,
                type -> {
                    builder.append(ReflectorUtils.getTypeName(type, declaredClasses));
                    builder.append(JavaFileConfig.SPACE);
                    builder.append(JavaFileConfig.createArgumentName(counter[0]));
                    counter[0]++;
                },
                builder);
        builder.append(JavaGrammarTerminals.RIGHT_PAREN);
    }

    static void appendThrowsList(@NonNull Type[] exceptionsTypes, @NonNull Set<@NonNull Class<?>> declaredClasses, @NonNull StringBuilder builder) {
        if (exceptionsTypes.length != 0) {
            builder.append(JavaFileConfig.SPACE);
            builder.append(JavaGrammarTerminals.THROWS_STRING);
            builder.append(JavaFileConfig.SPACE);
            StringBuilderUtils.appendTypesString(exceptionsTypes,
                    type -> builder.append(ReflectorUtils.getTypeName(type, declaredClasses)),
                    builder);
        }
    }

    private static void appendReturnDefaultValue(@NonNull Class<?> returnValueClass, @NonNull StringBuilder builder) {
        builder.append(JavaFileConfig.TAB);
        builder.append(JavaGrammarTerminals.RETURN_STRING);
        builder.append(JavaFileConfig.SPACE);
        builder.append(ReflectorUtils.createDefaultValueString(returnValueClass));
        builder.append(JavaGrammarTerminals.SEMICOLON);
        builder.append(JavaFileConfig.NEW_LINE);
    }

    private static void appendMethodBody(@NonNull Method method, @NonNull String indent, @NonNull StringBuilder builder) {
        int modifiers = method.getModifiers();
        if (!Modifier.isAbstract(modifiers) && !Modifier.isNative(modifiers)) {
            builder.append(JavaFileConfig.SPACE);
            builder.append(JavaGrammarTerminals.LEFT_BRACE);
            builder.append(JavaFileConfig.NEW_LINE);
            if (method.getReturnType() != void.class) {
                builder.append(indent);
                appendReturnDefaultValue(method.getReturnType(), builder);
            }
            builder.append(indent);
            builder.append(JavaGrammarTerminals.RIGHT_BRACE);
        } else {
            builder.append(JavaGrammarTerminals.SEMICOLON);
        }
    }
}
