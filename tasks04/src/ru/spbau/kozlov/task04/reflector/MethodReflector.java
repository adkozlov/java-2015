package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaCodeStyleConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;
import ru.spbau.kozlov.task04.reflector.utils.ReflectorUtils;
import ru.spbau.kozlov.task04.reflector.utils.StringBuilderUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * The {@link MethodReflector} class analyses modifiers and the list of exceptions that can be thrown of the specified constructor.
 *
 * @author adkozlov
 */
public final class MethodReflector {

    private MethodReflector() {
    }

    /**
     * Creates a string representation of the specified method.
     * Modifiers, list of exceptions that can be thrown are included.
     * The generated method returns a default value.
     * <p/>
     * Classes contained in the declared classes set are represented with their simple names.
     *
     * @param method          the specified method
     * @param indent          an indent to be used
     * @param declaredClasses a set of classes defined in the original class to be reflected
     * @return a string representation of the specified method
     */
    @NonNull
    public static String createMethodString(@NonNull Method method, @NonNull String indent, @NonNull Set<@NonNull Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        builder.append(indent);
        appendModifiers(method, builder);

        String typeParameters = ReflectorUtils.createTypeParametersString(method.getTypeParameters(), declaredClasses);
        if (!typeParameters.isEmpty()) {
            builder.append(typeParameters);
            builder.append(JavaCodeStyleConfig.SPACE);
        }
        builder.append(ReflectorUtils.getTypeName(method.getGenericReturnType(), declaredClasses));
        builder.append(JavaCodeStyleConfig.SPACE);
        builder.append(method.getName());

        StringBuilderUtils.appendArgumentsList(method.getGenericParameterTypes(), declaredClasses, builder);
        StringBuilderUtils.appendThrowsList(method.getGenericExceptionTypes(), declaredClasses, builder);
        appendMethodBody(method, indent, builder);
        builder.append(JavaCodeStyleConfig.NEW_LINE);
        builder.append(JavaCodeStyleConfig.NEW_LINE);

        return builder.toString();
    }

    private static void appendModifiers(@NonNull Method method, @NonNull StringBuilder builder) {
        int modifiers = method.getModifiers();
        if (method.getDeclaringClass().isInterface() && !Modifier.isAbstract(modifiers)) {
            builder.append(JavaGrammarTerminals.DEFAULT_STRING);
            builder.append(JavaCodeStyleConfig.SPACE);
        } else {
            StringBuilderUtils.appendModifiersString(modifiers, builder);
        }
    }

    private static void appendReturnDefaultValue(@NonNull Class<?> returnValueClass, @NonNull StringBuilder builder) {
        builder.append(JavaCodeStyleConfig.TAB);
        builder.append(JavaGrammarTerminals.RETURN_STRING);
        builder.append(JavaCodeStyleConfig.SPACE);
        builder.append(ReflectorUtils.createDefaultValueString(returnValueClass));
        builder.append(JavaGrammarTerminals.SEMICOLON);
        builder.append(JavaCodeStyleConfig.NEW_LINE);
    }

    private static void appendMethodBody(@NonNull Method method, @NonNull String indent, @NonNull StringBuilder builder) {
        int modifiers = method.getModifiers();
        if (!Modifier.isAbstract(modifiers) && !Modifier.isNative(modifiers)) {
            builder.append(JavaCodeStyleConfig.SPACE);
            builder.append(JavaGrammarTerminals.LEFT_BRACE);
            builder.append(JavaCodeStyleConfig.NEW_LINE);
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
