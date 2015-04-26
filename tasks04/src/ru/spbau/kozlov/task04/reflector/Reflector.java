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

    @NonNull
    public static String printStructure(@NonNull Class<?> clazz) throws IOException {
        String fileName = clazz.getSimpleName() + JavaFileConfig.JAVA_FILE_EXTENSION;
        Package pkg = clazz.getPackage();
        if (pkg != null) {
            String path = pkg.getName().replace(JavaGrammarTerminals.PACKAGE_DELIMITER, File.separatorChar);
            Files.createDirectories(Paths.get(path));
            fileName = path + File.separatorChar + fileName;
        }

        String result = createStructureString(clazz);
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            writer.write(result);
        }
        return result;
    }

    public static void diffClasses(@NonNull Class<?> first, @NonNull Class<?> second) {

    }

    @NonNull
    private static String createStructureString(@NonNull Class<?> clazz) {
        return createPackageString(clazz.getPackage()) + createStructureString(clazz, "", getAllDeclaredClasses(clazz));
    }

    @NonNull
    public static String createStructureString(@NonNull Class<?> clazz, @NonNull String indent, @NonNull Set<@NonNull Class<?>> declaredClasses) {
        StringBuilder builder = new StringBuilder();

        builder.append(indent);
        builder.append(ClassReflector.createClassHeader(clazz, declaredClasses));
        builder.append(JavaFileConfig.SPACE);
        builder.append(JavaGrammarTerminals.LEFT_BRACE);
        builder.append(JavaFileConfig.NEW_LINE);
        builder.append(JavaFileConfig.NEW_LINE);

        String newIndent = indent + JavaFileConfig.TAB;
        if (clazz != Object.class) {
            for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
                if (!declaredClass.isAnonymousClass()) {
                    builder.append(createStructureString(declaredClass, newIndent, declaredClasses));
                    builder.append(JavaFileConfig.NEW_LINE);
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

        builder.append(indent);
        builder.append(JavaGrammarTerminals.RIGHT_BRACE);
        builder.append(JavaFileConfig.NEW_LINE);

        return builder.toString();
    }

    @NonNull
    private static Set<@NonNull Class<?>> getAllDeclaredClasses(@NonNull Class<?> clazz) {
        Set<Class<?>> result = new HashSet<>();

        result.add(clazz);
        for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
            result.addAll(getAllDeclaredClasses(declaredClass));
        }

        return result;
    }

    @NonNull
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
