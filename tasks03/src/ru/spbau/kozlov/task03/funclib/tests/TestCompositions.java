package ru.spbau.kozlov.task03.funclib.tests;

import org.junit.Test;
import ru.spbau.kozlov.task03.funclib.core.Function;
import ru.spbau.kozlov.task03.funclib.core.Function2;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * The {@link TestCompositions} class tests compositions of functions and curring.
 *
 * @author adkozlov
 */
public class TestCompositions {

    /**
     * Test the composition of {@link ru.spbau.kozlov.task03.funclib.core.Function} instances.
     *
     * @see ru.spbau.kozlov.task03.funclib.core.Function#then
     */
    @Test
    public void testFunctionThen() {
        List<Integer> testNumbers = Arrays.asList(4, 8, 15, 16, 23, 42);
        Function<Integer, Double> composition = new Function<Integer, Double>() {
            @Override
            public Double apply(Integer argument) {
                return Math.sqrt(argument);
            }
        }.then(new Function<Double, Double>() {
            @Override
            public Double apply(Double argument) {
                return argument * argument;
            }
        });

        for (int number : testNumbers) {
            assertEquals((double) number, composition.apply(number), 1e-9);
        }
    }

    /**
     * Test the composition of {@link ru.spbau.kozlov.task03.funclib.core.Function2} instances.
     *
     * @see ru.spbau.kozlov.task03.funclib.core.Function2#then
     */
    @Test
    public void testFunction2Then() {
        List<Integer> testNumbers = Arrays.asList(4, 8, 15, 16, 23, 42);
        Function2<Integer, Integer, Integer> sumSqr = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer firstArgument, Integer secondArgument) {
                return firstArgument + secondArgument;
            }
        }.then(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer argument) {
                return argument * argument;
            }
        });

        for (int number : testNumbers) {
            assertEquals("Wrong composition value", 4 * number * number, (int) sumSqr.apply(number, number));
        }
    }

    /**
     * Tests currying of {@link ru.spbau.kozlov.task03.funclib.core.Function2} instances.
     *
     * @see ru.spbau.kozlov.task03.funclib.core.Function2#bind1
     * @see ru.spbau.kozlov.task03.funclib.core.Function2#bind2
     */
    @Test
    public void testCurrying() {
        List<Integer> testNumbers = Arrays.asList(4, 8, 15, 16, 23, 42);
        Function2<Integer, Integer, Integer> sum = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer firstArgument, Integer secondArgument) {
                return firstArgument + secondArgument;
            }
        };
        Function<Integer, Integer> plus2Left = sum.bind1(2);
        Function<Integer, Integer> plus3Right = sum.bind2(3);

        for (int number : testNumbers) {
            assertEquals("Wrong operation result", number + 2, (int) plus2Left.apply(number));
            assertEquals("Wrong operation result", number + 3, (int) plus3Right.apply(number));
        }
    }
}
