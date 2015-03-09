package ru.spbau.kozlov.task02;

import org.jetbrains.annotations.NotNull;
import ru.spbau.kozlov.task02.zip.ZipCompressor;
import ru.spbau.kozlov.task02.zip.ZipDecompressor;
import ru.spbau.kozlov.task02.zip.ZipLister;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * The {@link ru.spbau.kozlov.task02.Main} class implements zip-archiver command-line tool.
 * Files, directories and web pages are allowed. Empty directories are ignored.
 * If file or directory cannot be read or written to, it is skipped. Invalid URLs are also skipped.
 *
 * @author adkozlov
 */
public class Main {

    /**
     * Command-line interface.
     *
     * @param args first argument: run option, second argument: input/output file path, third argument: list of files (optional)
     */
    public static void main(@NotNull String[] args) {
        if (args.length < 2) {
            printUsageAndExit();
        }

        Path path = Paths.get(args[1]);
        switch (args[0]) {
            case "compress":
                compress(path, Arrays.copyOfRange(args, 2, args.length));
                break;
            case "decompress":
                decompress(path);
                break;
            case "list":
                list(path);
                break;
            default:
                printUsageAndExit();
        }
    }

    private static void compress(@NotNull Path outputFilePath, @NotNull String[] entries) {
        try (ZipCompressor zipCompressor = new ZipCompressor(outputFilePath)) {
            for (String entry : entries) {
                zipCompressor.putNextEntry(entry);
            }
        } catch (IOException | SecurityException e) {
            printExceptionsRecursively(e);
        }
    }

    private static void decompress(@NotNull Path inputFilePath) {
        try (ZipDecompressor zipDecompressor = new ZipDecompressor(inputFilePath)) {
            zipDecompressor.extractAllEntries();
        } catch (IOException | SecurityException e) {
            printExceptionsRecursively(e);
        }
    }

    private static void list(@NotNull Path inputFilePath) {
        try (ZipLister zipLister = new ZipLister(inputFilePath)) {
            System.out.println(zipLister.listAllEntries());
        } catch (IOException | SecurityException e) {
            printExceptionsRecursively(e);
        }
    }

    private static void printExceptionsRecursively(@NotNull Throwable throwable) {
        System.err.println("Error occurred: " + throwable);
        for (Throwable suppressed : throwable.getSuppressed()) {
            printExceptionsRecursively(suppressed);
        }
    }

    private static void printUsageAndExit() {
        System.out.println("Usage: Main " +
                "(compress <output_filename> [<filename>]) | " +
                "(decompress | list <input_filename>)");
        System.exit(1);
    }
}
