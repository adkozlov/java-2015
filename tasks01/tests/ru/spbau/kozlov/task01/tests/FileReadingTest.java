package ru.spbau.kozlov.task01.tests;

import org.junit.After;
import org.junit.Test;
import ru.spbau.kozlov.task01.messages.IllegalMessageFormatException;
import ru.spbau.kozlov.task01.messages.Message;
import ru.spbau.kozlov.task01.messages.readers.FileMessageReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.*;

/**
 * @author adkozlov
 */
public class FileReadingTest {

    public static final String TEST_FILE_NAME = "testFiles/reading.in";

    @After
    public void tearDown() {
        File testFile = new File(TEST_FILE_NAME);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testEmptyFile() throws IOException, IllegalMessageFormatException {
        createTestFile();

        try (FileMessageReader reader = new FileMessageReader(TEST_FILE_NAME)) {
            assertNull("some message read", reader.readMessage());
        }
    }

    @Test
    public void testOneCorrectMessage() throws IOException, IllegalMessageFormatException {
        createTestFile("3", "foo", "bar", "baz");

        try (FileMessageReader reader = new FileMessageReader(TEST_FILE_NAME)) {
            Message message = reader.readMessage();

            assertNotNull("message is not read", message);
            assertEquals("wrong lines count", 3, message.getLines().size());
            assertEquals("wrong first line", "foo", message.getLines().get(0));
            assertEquals("wrong second line", "bar", message.getLines().get(1));
            assertEquals("wrong third line", "baz", message.getLines().get(2));

            assertNull("some message read", reader.readMessage());
        }
    }

    @Test
    public void testTwoCorrectMessages() throws IOException, IllegalMessageFormatException {
        createTestFile("2", "foo", "bar", "1", "baz baz");

        try (FileMessageReader reader = new FileMessageReader(TEST_FILE_NAME)) {
            Message message = reader.readMessage();

            assertNotNull("message is not read", message);
            assertEquals("wrong lines count", 2, message.getLines().size());
            assertEquals("wrong first line", "foo", message.getLines().get(0));
            assertEquals("wrong second line", "bar", message.getLines().get(1));

            message = reader.readMessage();
            assertNotNull("message is not read", message);
            assertEquals("wrong lines count", 1, message.getLines().size());
            assertEquals("wrong first line", "baz baz", message.getLines().get(0));

            assertNull("some message read", reader.readMessage());
        }
    }

    @Test(expected = IllegalMessageFormatException.class)
    public void testIncorrectSizeFormat() throws IOException, IllegalMessageFormatException {
        createTestFile("foo", "bar");

        try (FileMessageReader reader = new FileMessageReader(TEST_FILE_NAME)) {
            reader.readMessage();
        }
    }

    @Test(expected = IllegalMessageFormatException.class)
    public void testIncorrectLinesCount() throws IOException, IllegalMessageFormatException {
        createTestFile("3", "foo", "bar");

        try (FileMessageReader reader = new FileMessageReader(TEST_FILE_NAME)) {
            reader.readMessage();
        }
    }

    private static void createTestFile(String... lines) throws FileNotFoundException {
        try (PrintWriter printWriter = new PrintWriter(TEST_FILE_NAME)) {
            for (String line : lines) {
                printWriter.println(line);
            }
        }
    }
}
