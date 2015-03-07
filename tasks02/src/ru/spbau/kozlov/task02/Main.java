package ru.spbau.kozlov.task02;

import org.jetbrains.annotations.NotNull;
import ru.spbau.kozlov.task02.zip.ZipCompressor;
import ru.spbau.kozlov.task02.zip.ZipDecompressor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * The {@link ru.spbau.kozlov.task02.Main} class implements zip-archiver.
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
                decompress(path, true);
                break;
            case "list":
                decompress(path, false);
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
        } catch (MalformedURLException e) {
            System.err.println("URL is not valid: " + e.getMessage());
            printSuppressedExceptions(e);
        } catch (IOException e) {
            System.err.println("I/O error occurred during compression: " + e.getMessage());
            printSuppressedExceptions(e);
        }
    }

    private static void decompress(@NotNull Path inputFilePath, boolean write) {
        try (ZipDecompressor zipDecompressor = new ZipDecompressor(inputFilePath)) {
            if (write) {
                zipDecompressor.extractAllEntries();
            } else {
                zipDecompressor.listAllEntries();
            }
        } catch (IOException e) {
            System.err.println("I/O error occurred during decompression: " + e.getMessage());
            printSuppressedExceptions(e);
        }
    }

    private static void printSuppressedExceptions(@NotNull Throwable throwable) {
        System.err.println("Error occurred: " + throwable);
        for (Throwable suppressed : throwable.getSuppressed()) {
            printSuppressedExceptions(suppressed);
        }
    }

    private static void printUsageAndExit() {
        System.out.println("Usage: Main " +
                "(compress <output_filename> [<filename>]) | " +
                "(decompress | list <input_filename>)");
        System.exit(1);
    }
}
