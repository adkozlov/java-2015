package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaFileConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;
import ru.spbau.kozlov.task04.reflector.utils.ReflectorUtils;
import ru.spbau.kozlov.task04.reflector.utils.StringBuilderUtils;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author adkozlov
 */
public final class ClassReflector {

    private ClassReflector() {
    }

    public static String createClassHeader(@NonNull Class<?> clazz, @NonNull Set<Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        boolean isInterface = clazz.isInterface();
        StringBuilderUtils.appendModifiersString(clazz.getModifiers(), builder);
        if (!isInterface) {
            builder.append(JavaGrammarTerminals.CLASS_STRING);
            builder.append(JavaFileConfig.SPACE);
        }
        builder.append(clazz.getSimpleName());

        builder.append(ReflectorUtils.createTypeParametersString(clazz.getTypeParameters(), declaredClasses));
        appendGenericSuperclass(clazz.getGenericSuperclass(), declaredClasses, builder);
        appendGenericInterfaces(isInterface, clazz.getGenericInterfaces(), declaredClasses, builder);

        return builder.toString();
    }

    private static void appendGenericSuperclass(Type superClassType, @NonNull Set<Class<?>> declaredClasses, @NonNull StringBuilder result) {
        if (superClassType != null && superClassType != Object.class) {
            result.append(JavaFileConfig.SPACE);
            result.append(JavaGrammarTerminals.EXTENDS_STRING);
            result.append(JavaFileConfig.SPACE);
            result.append(ReflectorUtils.getTypeName(superClassType, declaredClasses));
        }
    }

    private static void appendGenericInterfaces(boolean isInterface, @NonNull Type[] interfacesTypes, @NonNull Set<Class<?>> declaredClasses, @NonNull StringBuilder result) {
        if (interfacesTypes.length != 0) {
            result.append(JavaFileConfig.SPACE);
            result.append(isInterface ? JavaGrammarTerminals.EXTENDS_STRING : JavaGrammarTerminals.IMPLEMENTS_STRING);
            result.append(JavaFileConfig.SPACE);
            result.append(ReflectorUtils.createGenericTypesEnumerationString(interfacesTypes, String.valueOf(JavaGrammarTerminals.COMMA), declaredClasses));
        }
    }
}
