package ru.spbau.kozlov.task01;

import org.jetbrains.annotations.NotNull;
import ru.spbau.kozlov.task01.messages.IllegalMessageFormatException;
import ru.spbau.kozlov.task01.messages.Message;
import ru.spbau.kozlov.task01.messages.readers.FileMessageReader;
import ru.spbau.kozlov.task01.messages.writers.CompressMessageWriter;
import ru.spbau.kozlov.task01.messages.writers.ConsoleMessageWriter;
import ru.spbau.kozlov.task01.messages.writers.FileMessageWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The {@link ru.spbau.kozlov.task01.Main} class implements an application that reads the list of messages from the specified file, then zips every two message in one, and writes it to the standard output or to the specified file.
 *
 * @author adkozlov
 */
public class Main {

    /**
     * Command-line interface.
     *
     * @param args the name of file to read from, the name of file to write to (optional)
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Main <input_filename> [<output_filename>]");
            return;
        }

        String inputFileName = args[0];
        try (FileMessageReader messageReader = new FileMessageReader(inputFileName);
             CompressMessageWriter messageWriter = createCompressMessageWriter(args)) {
            Message message;
            while ((message = messageReader.readMessage()) != null) {
                messageWriter.writeMessage(message);
            }
        } catch (FileNotFoundException e) {
            System.err.printf("File not found: %s\n", e.getMessage());
            printSuppressedExceptions(e);
        } catch (IllegalMessageFormatException e) {
            System.err.printf("%s, line %d: %s\n", inputFileName, e.getLineNumber(), e.getMessage());
            printSuppressedExceptions(e);
        } catch (IOException e) {
            printIOException(e);
        }
    }

    @NotNull
    private static CompressMessageWriter createCompressMessageWriter(@NotNull String[] args) throws IOException {
        return new CompressMessageWriter(args.length == 1 ? new ConsoleMessageWriter() : new FileMessageWriter(args[1]));
    }

    private static void printIOException(@NotNull IOException e) {
        System.err.printf("I/O error occurred: %s\n", e.getMessage());
        printSuppressedExceptions(e);
    }

    private static void printSuppressedExceptions(@NotNull Throwable throwable) {
        Throwable[] suppressed = throwable.getSuppressed();
        if (suppressed.length > 0) {
            for (Throwable innerThrowable : suppressed) {
                if (innerThrowable instanceof IOException) {
                    printIOException((IOException) innerThrowable);
                } else {
                    System.err.printf("Error occurred: %s, type: %s\n", innerThrowable.getMessage(), innerThrowable.getClass());
                    printSuppressedExceptions(innerThrowable);
                }
            }
        }
    }
}
