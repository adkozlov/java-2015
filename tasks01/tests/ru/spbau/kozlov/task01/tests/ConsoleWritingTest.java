package ru.spbau.kozlov.task01.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.kozlov.task01.messages.Message;
import ru.spbau.kozlov.task01.messages.writers.ConsoleMessageWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * @author adkozlov
 */
public class ConsoleWritingTest {

    private final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errStream = new ByteArrayOutputStream();

    private PrintStream oldOutStream;
    private PrintStream oldErrStream;

    @Before
    public void setUp() {
        oldOutStream = System.out;
        System.setOut(new PrintStream(outStream));

        oldErrStream = System.err;
        System.setErr(new PrintStream(errStream));
    }

    @After
    public void tearDown() throws IOException {
        System.setOut(oldOutStream);
        outStream.close();

        System.setErr(oldErrStream);
        errStream.close();
    }

    @Test
    public void testEmptyStream() throws IOException {
        try (ConsoleMessageWriter writer = new ConsoleMessageWriter()) {
        }

        assertEquals("not empty output stream", "", outStream.toString());
        assertEquals("not empty error stream", "", errStream.toString());
    }

    @Test
    public void testOneMessage() throws IOException {
        try (ConsoleMessageWriter writer = new ConsoleMessageWriter()) {
            writer.writeMessage(new Message("foo", "bar"));
        }

        assertEquals("wrong output", "Message 1\n1.1. foo\n1.2. bar\n", outStream.toString());
        assertEquals("not empty error stream", "", errStream.toString());
    }

    @Test
    public void testTwoMessages() throws IOException {
        try (ConsoleMessageWriter writer = new ConsoleMessageWriter()) {
            writer.writeMessage(new Message("foo", "bar"));
            writer.writeMessage(new Message("baz baz"));
        }

        assertEquals("wrong output", "Message 1\n1.1. foo\n1.2. bar\nMessage 2\n2.1. baz baz\n", outStream.toString());
        assertEquals("not empty error stream", "", errStream.toString());
    }
}
