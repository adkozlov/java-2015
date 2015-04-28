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
 * The {@link Compiler} class compiles Java classes from the source code.
 *
 * @author adkozlov
 */
public final class Compiler {

    private static final File PATH = new File(".");

    private Compiler() {
    }

    /**
     * Compiles a Java class from the source code and loads it.
     *
     * @param simpleName the simple name of the class to be compiled and loaded
     * @param source     the source code
     * @return the loaded class object
     * @throws ClassNotFoundException if the compiled class cannot be loaded
     * @throws IOException            if the compiler output directory cannot be found
     * @throws CompilationException   if a compilation error occurs
     */
    public static Class<?> compileAndLoad(@NonNull String simpleName, @NonNull String source) throws ClassNotFoundException, IOException, CompilationException {
        String newSimpleName = CompilerUtils.getUniqueName(simpleName);
        String newCanonicalName = CompilerUtils.getCanonicalName(newSimpleName, CompilerUtils.getPackageName(source));
        String newSource = CompilerUtils.updateSource(simpleName, newSimpleName, source);
        if (compile(new StringJavaSource(newCanonicalName, newSource))) {
            return loadClass(newCanonicalName);
        } else {
            throw new CompilationException(newCanonicalName);
        }
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
