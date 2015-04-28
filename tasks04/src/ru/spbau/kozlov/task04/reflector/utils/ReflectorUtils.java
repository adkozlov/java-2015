package ru.spbau.kozlov.task04.reflector.utils;

import checkers.nullness.quals.NonNull;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@link ReflectorUtils} class contains methods for correct types string representation and comparison.
 *
 * @author adkozlov
 */
public final class ReflectorUtils {

    private ReflectorUtils() {
    }

    /**
     * Returns a string representation of the specified type.
     *
     * Classes contained in the declared classes set are represented with their simple names.
     *
     * @param type the specified type
     * @param declaredClasses a set of classes defined in the original class to be reflected
     * @return a string representation
     */
    @NonNull
    public static String getTypeName(@NonNull Type type, @NonNull Set<@NonNull Class<?>> declaredClasses) {
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

    @NonNull
    private static String getSimpleName(@NonNull Type type, @NonNull Set<@NonNull Class<?>> declaredClasses) {
        String result = type.getTypeName().replace(JavaGrammarTerminals.INNER_CLASS_DELIMITER, JavaGrammarTerminals.NESTED_CLASS_DELIMITER);
        return declaredClasses.contains(type) ? result.substring(result.lastIndexOf(JavaGrammarTerminals.NESTED_CLASS_DELIMITER) + 1) : result;
    }

    /**
     * Returns a string representation of the default value of the specified class.
     *
     * @param returnValueClass the specified class
     * @return the default value
     */
    @NonNull
    public static String createDefaultValueString(@NonNull Class<?> returnValueClass) {
        if (!returnValueClass.isPrimitive()) {
            return JavaGrammarTerminals.NULL_STRING;
        } else if (returnValueClass != boolean.class) {
            return JavaGrammarTerminals.ZERO_STRING;
        } else {
            return JavaGrammarTerminals.FALSE_STRING;
        }
    }

    /**
     * Returns a string representation of the list of generic types separated with the specified delimiter.
     *
     * Classes contained in the declared classes set are represented with their simple names.
     *
     * @param types an array of types
     * @param delimiter the specified delimiter
     * @param declaredClasses a set of classes defined in the original class to be reflected
     * @return a string representation
     */
    @NonNull
    public static String createGenericTypesEnumerationString(@NonNull Type[] types, @NonNull String delimiter, @NonNull Set<@NonNull Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        List<Type> typeList = Arrays.asList(types).stream().filter(type -> type != Object.class).collect(Collectors.toList());
        StringBuilderUtils.appendTypesString(typeList.toArray(new Type[typeList.size()]),
                delimiter,
                type -> builder.append(getTypeName(type, declaredClasses)),
                builder);

        return builder.toString();
    }

    /**
     * Returns a string representation of a sequence of the type parameters.
     *
     * Classes contained in the declared classes set are represented with their simple names.
     *
     * @param typeParameters an array of type parameters
     * @param declaredClasses a set of classes defined in the original class to be reflected
     * @return a string representation
     */
    @NonNull
    public static String createTypeParametersString(@NonNull TypeVariable<?>[] typeParameters, @NonNull Set<@NonNull Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        StringBuilderUtils.appendTypeParametersString(typeParameters, typeVariable -> {
            builder.append(typeVariable.getName());
            StringBuilderUtils.appendBoundsString(typeVariable.getBounds(), true, declaredClasses, builder);
        }, builder);

        return builder.toString();
    }

    /**
     * Checks if two specified generic types are equal.
     *
     * @param first the first generic type
     * @param second the second generic type
     * @return {@code true} if the types are equal
     */
    public static boolean checkGenericTypesAreEqual(Type first, Type second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null) {
            return false;
        }
        if (first.equals(second)) {
            return true;
        }
        if (second == null) {
            return false;
        }
        if (!(first instanceof TypeVariable<?>) || !(second instanceof TypeVariable<?>)) {
            if (!(first instanceof ParameterizedType) || !(second instanceof ParameterizedType)) {
                if (!(first instanceof GenericArrayType) || !(second instanceof GenericArrayType)) {
                    if (!(first instanceof WildcardType) || !(second instanceof WildcardType)) {
                        return false;
                    }

                    return checkWildcardTypesAreEqual((WildcardType) first, (WildcardType) second);
                }

                return checkGenericArrayTypesAreEqual((GenericArrayType) first, (GenericArrayType) second);
            }

            return checkParametrizedTypesAreEqual((ParameterizedType) first, (ParameterizedType) second);
        }

        return checkTypeVariablesAreEqual((TypeVariable<?>) first, (TypeVariable<?>) second);
    }

    private static boolean checkWildcardTypesAreEqual(@NonNull WildcardType first, @NonNull WildcardType second) {
        return checkGenericTypesAreEqual(first.getLowerBounds(), second.getLowerBounds()) && checkGenericTypesAreEqual(first.getUpperBounds(), second.getUpperBounds());
    }

    private static boolean checkGenericArrayTypesAreEqual(@NonNull GenericArrayType first, @NonNull GenericArrayType second) {
        return checkGenericTypesAreEqual(first.getGenericComponentType(), second.getGenericComponentType());
    }

    private static boolean checkParametrizedTypesAreEqual(@NonNull ParameterizedType first, @NonNull ParameterizedType second) {
        if (!first.getRawType().equals(second.getRawType())) {
            return false;
        }
        return checkGenericTypesAreEqual(first.getActualTypeArguments(), second.getActualTypeArguments());
    }

    private static boolean checkTypeVariablesAreEqual(@NonNull TypeVariable<?> first, @NonNull TypeVariable<?> second) {
        if (!first.getName().equals(second.getName())) {
            return false;
        }
        return !(!typeVariableBoundIsObject(first) || !typeVariableBoundIsObject(second));
    }

    private static boolean typeVariableBoundIsObject(@NonNull TypeVariable<?> typeVariable) {
        return typeVariable.getBounds() == null || Arrays.equals(typeVariable.getBounds(), new Object[]{Object.class});
    }

    /**
     * Checks if two arrays of generic types are equal.
     *
     * @param firstClassParameterTypes the first array of generic types
     * @param secondClassParameterTypes the second array of generic types
     * @return {@code true} if the arrays are equal
     */
    public static boolean checkGenericTypesAreEqual(Type[] firstClassParameterTypes, Type[] secondClassParameterTypes) {
        if (firstClassParameterTypes == null && secondClassParameterTypes == null) {
            return true;
        }
        if (firstClassParameterTypes == null || secondClassParameterTypes == null) {
            return false;
        }
        if (firstClassParameterTypes.length != secondClassParameterTypes.length) {
            return false;
        }

        for (int i = 0; i < firstClassParameterTypes.length; i++) {
            if (!checkGenericTypesAreEqual(firstClassParameterTypes[i], secondClassParameterTypes[i])) {
                return false;
            }
        }
        return true;
    }
}
