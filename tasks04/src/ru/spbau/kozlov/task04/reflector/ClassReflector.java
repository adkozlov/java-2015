package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaCodeStyleConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
     * @param clazz
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
}
