package ru.spbau.kozlov.task04.compiler;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task04.compiler.utils.CompilerUtils;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;

/**
 * @author adkozlov
 */
public final class StringJavaSourceCompiler {

    private static final File PATH = new File(".");

    private StringJavaSourceCompiler() {
    }

    public static Class<?> compileAndLoad(@NonNull String simpleName, @NonNull String source) throws ClassNotFoundException, IOException {
        String newSimpleName = CompilerUtils.getUniqueName(simpleName);
        String newCanonicalName = CompilerUtils.getCanonicalName(newSimpleName, CompilerUtils.getPackageName(source));
        String newSource = CompilerUtils.updateSource(simpleName, newSimpleName, source);
        return compile(new StringJavaSource(newCanonicalName, newSource)) ? loadClass(newCanonicalName) : null;
    }

    private static boolean compile(@NonNull StringJavaSource stringJavaSource) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(PATH));
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, new DiagnosticCollector<>(), null, null, Collections.singletonList(stringJavaSource));
            return task.call();
        }
    }

    @NonNull
    private static Class<?> loadClass(@NonNull String canonicalName) throws MalformedURLException, ClassNotFoundException {
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{PATH.toURI().toURL()});
        return classLoader.loadClass(canonicalName);
    }
}
