package ru.spbau.kozlov.task01.tests;

import org.junit.After;
import org.junit.Test;
import ru.spbau.kozlov.task01.messages.Message;
import ru.spbau.kozlov.task01.messages.writers.FileMessageWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author adkozlov
 */
public class FileWritingTest {

    public static final String TEST_FILE_NAME = "testFiles/writing.out";

    @After
    public void tearDown() {
        File testFile = new File(TEST_FILE_NAME);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testEmptyFile() throws IOException {
        createTestFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE_NAME))) {
            assertNull("some line read", reader.readLine());
        }
    }

    @Test
    public void testOneMessage() throws IOException {
        createTestFile(new Message("foo", "bar"));

        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE_NAME))) {
            assertEquals("wrong lines count", "2", reader.readLine());
            assertEquals("wrong first line", "foo", reader.readLine());
            assertEquals("wrong second line", "bar", reader.readLine());

            assertNull("some line read", reader.readLine());
        }
    }

    @Test
    public void testTwoMessages() throws IOException {
        createTestFile(new Message("foo", "bar"), new Message("baz baz"));

        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE_NAME))) {
            assertEquals("wrong lines count of first message", "2", reader.readLine());
            assertEquals("wrong first line of first message", "foo", reader.readLine());
            assertEquals("wrong second line of first message", "bar", reader.readLine());

            assertEquals("wrong lines count of second message", "1", reader.readLine());
            assertEquals("wrong first line of second message", "baz baz", reader.readLine());

            assertNull("some line read", reader.readLine());
        }
    }

    private static void createTestFile(Message... messages) throws IOException {
        try (FileMessageWriter writer = new FileMessageWriter(TEST_FILE_NAME)) {
            for (Message message : messages) {
                writer.writeMessage(message);
            }
        }
    }
}
