package ru.spbau.kozlov.task03;

import org.junit.runner.JUnitCore;

/**
 * The {@link Main} class runs all the tests from the {@link ru.spbau.kozlov.task03.funclib.tests.FuncLibSuite} class.
 *
 * @author adkozlov
 */
public class Main {

    /**
     * The command-line interface.
     *
     * @param args command-line arugments
     */
    public static void main(String[] args) {
        JUnitCore.main("ru.spbau.kozlov.task03.funclib.tests.FuncLibSuite");
    }
}
