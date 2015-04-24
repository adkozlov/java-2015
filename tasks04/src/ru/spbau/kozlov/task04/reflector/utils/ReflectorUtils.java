package ru.spbau.kozlov.task04.reflector.utils;

import checkers.nullness.quals.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author adkozlov
 */
public final class ReflectorUtils {

    private ReflectorUtils() {
    }

    public static String getTypeName(@NonNull Type type, @NonNull Set<Class<?>> declaredClasses) {
        if (type instanceof WildcardType) {
            StringBuilder builder = new StringBuilder();

            WildcardType wildcardType = (WildcardType) type;
            builder.append(JavaGrammarTerminals.WILD_CARD);
            StringBuilderUtils.appendBoundsString(wildcardType.getUpperBounds(), true, declaredClasses, builder);
            StringBuilderUtils.appendBoundsString(wildcardType.getLowerBounds(), false, declaredClasses, builder);

            return builder.toString();
        }

        if (type instanceof ParameterizedType) {
            StringBuilder builder = new StringBuilder();

            ParameterizedType parameterizedType = (ParameterizedType) type;
            builder.append(getSimpleName(parameterizedType.getRawType(), declaredClasses));
            StringBuilderUtils.appendTypeParametersString(parameterizedType.getActualTypeArguments(),
                    typeArgument -> builder.append(getTypeName(typeArgument, declaredClasses)),
                    builder);

            return builder.toString();
        }

        return getSimpleName(type, declaredClasses);
    }

    private static String getSimpleName(@NonNull Type type, @NonNull Set<Class<?>> declaredClasses) {
        String result = type.getTypeName().replace(JavaGrammarTerminals.INNER_CLASS_DELIMITER, JavaGrammarTerminals.CLASS_DELIMITER);
        return declaredClasses.contains(type) ? result.substring(result.lastIndexOf(JavaGrammarTerminals.CLASS_DELIMITER) + 1) : result;
    }

    public static String createDefaultValueString(@NonNull Class<?> returnValueClass) {
        if (!returnValueClass.isPrimitive()) {
            return JavaGrammarTerminals.NULL_STRING;
        } else if (returnValueClass != boolean.class) {
            return JavaGrammarTerminals.ZERO_STRING;
        } else {
            return JavaGrammarTerminals.FALSE_STRING;
        }
    }

    public static String createGenericTypesEnumerationString(@NonNull Type[] types, @NonNull String delimiter, @NonNull Set<Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();
        List<Type> typeList = Arrays.asList(types).stream().filter(type -> type != Object.class).collect(Collectors.toList());
        StringBuilderUtils.appendTypesString(typeList.toArray(new Type[typeList.size()]),
                delimiter,
                type -> builder.append(getTypeName(type, declaredClasses)),
                builder);
        return builder.toString();
    }

    public static String createTypeParametersString(@NonNull TypeVariable<?>[] typeParameters, @NonNull Set<Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();
        StringBuilderUtils.appendTypeParametersString(typeParameters, typeVariable -> {
            builder.append(typeVariable.getName());
            StringBuilderUtils.appendBoundsString(typeVariable.getBounds(), true, declaredClasses, builder);
        }, builder);
        return builder.toString();
    }
}
