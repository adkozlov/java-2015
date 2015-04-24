package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaFileConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * @author adkozlov
 */
public final class Reflector {

    private Reflector() {
    }

    public static void printStructure(@NonNull Class<?> clazz) throws IOException {
        Package pkg = clazz.getPackage();
        String path = pkg.getName().replace(JavaGrammarTerminals.PACKAGE_DELIMITER, File.separatorChar) + File.separator;
        Files.createDirectories(Paths.get(path));

        String fileName = path + clazz.getSimpleName() + JavaFileConfig.JAVA_FILE_EXTENSION;
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            writer.write(createPackageString(pkg));
            writer.write(createStructureString(clazz));
        }
    }

    public static String createStructureString(@NonNull Class<?> clazz) {
        return createStructureString(clazz, "", getAllDeclaredClasses(clazz));
    }

    public static String createStructureString(@NonNull Class<?> clazz, @NonNull String indent, @NonNull Set<Class<?>> declaredClasses) {
        StringBuilder result = new StringBuilder();

        result.append(indent);
        result.append(ClassReflector.createClassHeader(clazz, declaredClasses));
        result.append(JavaFileConfig.SPACE);
        result.append(JavaGrammarTerminals.LEFT_BRACE);
        result.append(JavaFileConfig.NEW_LINE);
        result.append(JavaFileConfig.NEW_LINE);

        String newIndent = indent + JavaFileConfig.TAB;
        if (clazz != Object.class) {
            for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
                if (!declaredClass.isAnonymousClass()) {
                    result.append(createStructureString(declaredClass, newIndent, declaredClasses));
                    result.append(JavaFileConfig.NEW_LINE);
                }
            }
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isSynthetic()) {
                    result.append(FieldReflector.createFieldString(field, newIndent, declaredClasses));
                    result.append(JavaFileConfig.NEW_LINE);
                }
            }
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (!constructor.isSynthetic()) {
                    result.append(ConstructorReflector.createConstructorString(constructor, newIndent, declaredClasses));
                    result.append(JavaFileConfig.NEW_LINE);
                }
            }
            for (Method method : clazz.getDeclaredMethods()) {
                if (!method.isSynthetic()) {
                    result.append(MethodReflector.createMethodString(method, newIndent, declaredClasses));
                    result.append(JavaFileConfig.NEW_LINE);
                }
            }
        }

        if (!clazz.isInterface()) {
            try {
                try {
                    clazz.getDeclaredMethod("equals", Object.class);
                } catch (NoSuchMethodException e) {
                    result.append(MethodReflector.createMethodString(clazz.getMethod("equals", Object.class), newIndent, declaredClasses));
                    result.append(JavaFileConfig.NEW_LINE);
                }
                try {
                    clazz.getDeclaredMethod("hashCode");
                } catch (NoSuchMethodException e) {
                    result.append(MethodReflector.createMethodString(clazz.getMethod("hashCode"), newIndent, declaredClasses));
                    result.append(JavaFileConfig.NEW_LINE);
                }
                try {
                    clazz.getDeclaredMethod("toString");
                } catch (NoSuchMethodException e) {
                    result.append(MethodReflector.createMethodString(clazz.getMethod("toString"), newIndent, declaredClasses));
                }
            } catch (NoSuchMethodException e) {
                // cannot be
            }
        }

        result.append(indent);
        result.append(JavaGrammarTerminals.RIGHT_BRACE);
        result.append(JavaFileConfig.NEW_LINE);

        return result.toString();
    }

    private static Set<Class<?>> getAllDeclaredClasses(@NonNull Class<?> clazz) {
        Set<Class<?>> result = new HashSet<>();

        result.add(clazz);
        for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
            result.addAll(getAllDeclaredClasses(declaredClass));
        }

        return result;
    }

    private static String createPackageString(Package pkg) {
        StringBuilder builder = new StringBuilder();

        if (pkg != null) {
            builder.append(JavaGrammarTerminals.PACKAGE_STRING);
            builder.append(JavaFileConfig.SPACE);
            builder.append(pkg.getName());
            builder.append(JavaGrammarTerminals.SEMICOLON);
            builder.append(JavaFileConfig.NEW_LINE);
            builder.append(JavaFileConfig.NEW_LINE);
        }

        return builder.toString();
    }
}
