package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.reflector.utils.JavaCodeStyleConfig;
import ru.spbau.kozlov.task04.reflector.utils.JavaGrammarTerminals;

import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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

    /**
     *
     *
     * @param clazz the specified class
     * @return a string representation of the specified class structure
     * @throws IOException if an I/O error occurs
     */
    @NonNull
    public static String printStructure(@NonNull Class<?> clazz) throws IOException {
        String fileName = clazz.getSimpleName() + JavaFileObject.Kind.SOURCE.extension;
        Package pkg = clazz.getPackage();
        if (pkg != null) {
            String path = pkg.getName().replace(JavaGrammarTerminals.PACKAGE_DELIMITER, File.separatorChar);
            Files.createDirectories(Paths.get(path));
            fileName = path + File.separatorChar + fileName;
        }

        String result = createPackageString(clazz.getPackage()) + ClassReflector.createStructureString(clazz, "", getAllDeclaredClasses(clazz));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            writer.write(result);
        }
        return result;
    }

    /**
     * @param first
     * @param second
     */
    public static void diffClasses(@NonNull Class<?> first, @NonNull Class<?> second) {

    }

    private static @NonNull Set<@NonNull Class<?>> getAllDeclaredClasses(@NonNull Class<?> clazz) {
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
            builder.append(JavaCodeStyleConfig.SPACE);
            builder.append(pkg.getName());
            builder.append(JavaGrammarTerminals.SEMICOLON);
            builder.append(JavaCodeStyleConfig.NEW_LINE);
            builder.append(JavaCodeStyleConfig.NEW_LINE);
        }

        return builder.toString();
    }
}
