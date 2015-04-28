package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaCodeStyleConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * The {@link ClassReflector} class analyses fields, constructors, methods and inner/nested classes and interfaces of the specified class.
 *
 * @author adkozlov
 */
public final class ClassReflector {

    private ClassReflector() {
    }

    /**
     * Creates a string representation of the specified class or interface.
     * All the fields, constructors, methods and inner/nested classes and interfaces are included.
     * <p/>
     * Classes contained in the declared classes set are represented with their simple names.
     *
     * @param clazz the specified class
     * @param indent          an indent to be used
     * @param declaredClasses a set of classes defined in the original class to be reflected
     * @return a string representation of the specified class
     */
    @NonNull
    public static String createStructureString(@NonNull Class<?> clazz, @NonNull String indent, @NonNull Set<@NonNull Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        builder.append(ClassHeaderReflector.createClassHeader(clazz, indent, declaredClasses));
        builder.append(JavaCodeStyleConfig.SPACE);
        builder.append(JavaGrammarTerminals.LEFT_BRACE);
        builder.append(JavaCodeStyleConfig.NEW_LINE);
        builder.append(JavaCodeStyleConfig.NEW_LINE);

        if (clazz != Object.class) {
            String newIndent = indent + JavaCodeStyleConfig.TAB;
            for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
                if (!declaredClass.isAnonymousClass()) {
                    builder.append(createStructureString(declaredClass, newIndent, declaredClasses));
                    builder.append(JavaCodeStyleConfig.NEW_LINE);
                }
            }
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isSynthetic()) {
                    builder.append(FieldReflector.createFieldString(field, newIndent, declaredClasses));
                }
            }
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (!constructor.isSynthetic()) {
                    builder.append(ConstructorReflector.createConstructorString(constructor, newIndent, declaredClasses));
                }
            }
            for (Method method : clazz.getDeclaredMethods()) {
                if (!method.isSynthetic()) {
                    builder.append(MethodReflector.createMethodString(method, newIndent, declaredClasses));
                }
            }

            if (!clazz.isInterface()) {
                try {
                    try {
                        clazz.getDeclaredMethod("equals", Object.class);
                    } catch (NoSuchMethodException e) {
                        builder.append(MethodReflector.createMethodString(clazz.getMethod("equals", Object.class), newIndent, declaredClasses));
                    }
                    try {
                        clazz.getDeclaredMethod("hashCode");
                    } catch (NoSuchMethodException e) {
                        builder.append(MethodReflector.createMethodString(clazz.getMethod("hashCode"), newIndent, declaredClasses));
                    }
                    try {
                        clazz.getDeclaredMethod("toString");
                    } catch (NoSuchMethodException e) {
                        builder.append(MethodReflector.createMethodString(clazz.getMethod("toString"), newIndent, declaredClasses));
                    }
                } catch (NoSuchMethodException e) {
                    // cannot be
                }
            }
        }

        builder.append(indent);
        builder.append(JavaGrammarTerminals.RIGHT_BRACE);
        builder.append(JavaCodeStyleConfig.NEW_LINE);

        return builder.toString();
    }

    /**
     * Analyzes all the fields/constructors/methods/classes of the two specified classes and returns the difference.
     *
     * @param first the first class to be analyzed
     * @param second the second class to be analyzed
     * @return a set of {@link Difference} class instances
     */
    @NonNull
    public static Set<Difference> diffClasses(@NonNull Class<?> first, @NonNull Class<?> second) {
        Set<Difference> result = ClassHeaderReflector.diffClassHeaders(first, second);

        result.addAll(FieldReflector.diffFields(first, second));
        result.addAll(FieldReflector.diffFields(second, first));

        result.addAll(ConstructorReflector.diffConstructors(first, second));
        result.addAll(ConstructorReflector.diffConstructors(second, first));

        result.addAll(MethodReflector.diffMethods(first, second));
        result.addAll(MethodReflector.diffMethods(second, first));

        result.addAll(diffDeclaredClasses(first, second));
        result.addAll(diffDeclaredClasses(second, first));

        return result;
    }

    @NonNull
    private static Set<Difference> diffDeclaredClasses(@NonNull Class<?> first, @NonNull Class<?> second) {
        Set<Difference> result = new LinkedHashSet<>();

        Map<String, Class<?>> firstClassDeclaredClassesMap = createDeclaredClassesMap(first.getDeclaredClasses());
        Map<String, Class<?>> secondClassDeclaredClassesMap = createDeclaredClassesMap(second.getDeclaredClasses());

        for (Map.Entry<String, Class<?>> entry : firstClassDeclaredClassesMap.entrySet()) {
            Class<?> firstClass = entry.getValue();
            Class<?> secondClass = secondClassDeclaredClassesMap.get(entry.getKey());
            if (!secondClassDeclaredClassesMap.containsKey(entry.getKey())) {
                result.add(new Difference(firstClass.toGenericString(), second.toGenericString(), "Class \'%s\' is not defined in the class \'%s\'"));
            } else {
                diffClasses(firstClass, secondClass);
            }
        }

        return result;
    }

    private static Map<String, Class<?>> createDeclaredClassesMap(@NonNull Class<?>[] declaredClasses) {
        Map<String, Class<?>> firstClassDeclaredClassesMap = new HashMap<>();
        for (Class<?> firstClassDeclaredClass : declaredClasses) {
            firstClassDeclaredClassesMap.put(firstClassDeclaredClass.getSimpleName(), firstClassDeclaredClass);
        }
        return firstClassDeclaredClassesMap;
    }
}
