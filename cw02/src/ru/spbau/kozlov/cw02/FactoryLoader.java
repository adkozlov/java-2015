package ru.spbau.kozlov.cw02;

import checkers.nullness.quals.NonNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author adkozlov
 */
public class FactoryLoader {

    public void loadFactories(@NonNull File file) throws IOException, ClassNotFoundException {
        if (file.isFile()) {
            loadFactory(file);
        } else {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(file.toPath(), "*.class")) {
                for (Path path : directoryStream) {
                    loadFactory(path.toFile());
                }
            }
        }
    }

    private void loadFactory(@NonNull File file) throws ClassNotFoundException {
        URLClassLoader classLoader = null;
        try {
            classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
        } catch (MalformedURLException e) {
            // ignoring
        }
        assert classLoader != null;

        Class<?> factory = classLoader.loadClass(file.getName().replace(".class", "").replace(File.separatorChar, '.')).asSubclass(ConcreteFactory.class);

        try {
            ConcreteFactory concreteFactory = (ConcreteFactory) factory.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // ignoring
        }
    }
}
