package ru.spbau.kozlov.task03.tests;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.spbau.kozlov.task03.funclib.core.Function;
import ru.spbau.kozlov.task03.funclib.core.Function2;
import ru.spbau.kozlov.task03.funclib.core.Predicate;
import ru.spbau.kozlov.task03.funclib.prelude.Prelude;

import java.util.*;

import static org.junit.Assert.*;

/**
 * The {@link TestSecondOrderFunctions} class tests the second order functions such as {@code map, filter, take, foldr}
 *
 * @author adkozlov
 */
public class TestSecondOrderFunctions {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    /**
     * Tests the {@link ru.spbau.kozlov.task03.funclib.prelude.Prelude#foldr(ru.spbau.kozlov.task03.funclib.core.Function2)} function.
     */
    @Test
    public void testFoldr() {
        List<Integer> testNumbers = Arrays.asList(8, 12, 24, 4);
        Function2<Integer, Double, Double> divide = new Function2<Integer, Double, Double>() {
            @Nullable
            @Override
            public Double apply(@NotNull Integer firstArgument, @NotNull Double secondArgument) {
                return firstArgument / secondArgument;
            }
        };

        Function2<? super Double, ListIterator<? extends Integer>, Double> foldrFunction = Prelude.foldr(divide);
        assertNotNull("Foldr function is null", foldrFunction);
        assertEquals(8.0, foldrFunction.apply(2.0, testNumbers.listIterator(testNumbers.size())), 0.0);
    }

    /**
     * Tests the {@link ru.spbau.kozlov.task03.funclib.prelude.Prelude#foldr(ru.spbau.kozlov.task03.funclib.core.Function2, java.lang.Object, java.util.List)} function.
     */
    @Test
    public void testFoldrEmpty() {
        List<Integer> testNumbers = Arrays.asList();
        Function2<Integer, Integer, Integer> sum = new Function2<Integer, Integer, Integer>() {
            @Nullable
            @Override
            public Integer apply(@NotNull Integer firstArgument, @NotNull Integer secondArgument) {
                return firstArgument + secondArgument;
            }
        };

        assertEquals("Wrong foldr value", 5, (int) Prelude.foldr(sum, 5, testNumbers));
    }

    /**
     * Tests the {@link ru.spbau.kozlov.task03.funclib.prelude.Prelude#take(int, java.lang.Class)} function.
     */
    @Test
    public void testTake() {
        List<Integer> testNumbers = Arrays.asList(4, 8, 15, 16, 23, 42);

        Iterator<Integer> takeIterator = Prelude.take(3, Integer.class).apply(testNumbers.iterator());
        assertNotNull("Take iterator is null", takeIterator);
        assertEquals("Wrong taken value", 4, (int) takeIterator.next());
        assertEquals("Wrong taken value", 8, (int) takeIterator.next());
        assertEquals("Wrong taken value", 15, (int) takeIterator.next());

        assertFalse("Take iterator is not empty", takeIterator.hasNext());
        expectedException.expect(NoSuchElementException.class);
        takeIterator.next();
    }

    /**
     * Tests the {@link ru.spbau.kozlov.task03.funclib.prelude.Prelude#take(int, java.lang.Iterable)} function.
     */
    @Test
    public void testTakeEmpty() {
        List<Integer> testNumbers = Arrays.asList();

        Iterator<Integer> takeIterator = Prelude.take(5, testNumbers);
        assertFalse("Take iterator is not empty", takeIterator.hasNext());
        expectedException.expect(NoSuchElementException.class);
        takeIterator.next();
    }

    /**
     * Tests the {@link ru.spbau.kozlov.task03.funclib.prelude.Prelude#map(ru.spbau.kozlov.task03.funclib.core.Function, java.util.Iterator)} function.
     */
    @Test
    public void testMapSqrt() {
        List<Integer> testNumbers = Arrays.asList(4, 8, 15, 16, 23, 42);
        Function<Integer, Double> sqrt = new Function<Integer, Double>() {
            @Nullable
            @Override
            public Double apply(@NotNull Integer argument) {
                return Math.sqrt(argument);
            }
        };

        Iterator<Double> mapIterator = Prelude.map(sqrt).apply(testNumbers.iterator());
        assertNotNull("Filter iterator is null", mapIterator);

        for (int testNumber : testNumbers) {
            assertTrue("Filter iterator is empty", mapIterator.hasNext());
            assertEquals("Wrong square root value", (Double) Math.sqrt(testNumber), mapIterator.next());
        }

        assertFalse("Filter iterator is not empty", mapIterator.hasNext());
        expectedException.expect(NoSuchElementException.class);
        mapIterator.next();
    }

    /**
     * Tests the {@link ru.spbau.kozlov.task03.funclib.prelude.Prelude#map(ru.spbau.kozlov.task03.funclib.core.Function, java.lang.Iterable)} function.
     */
    @Test
    public void testMapEmpty() {
        List<Integer> testNumbers = Arrays.asList();
        Function<Integer, Integer> id = new Function<Integer, Integer>() {
            @Nullable
            @Override
            public Integer apply(@NotNull Integer argument) {
                return argument;
            }
        };

        Iterator<Integer> mapIterator = Prelude.map(id, testNumbers);
        assertFalse("Map iterator is not empty", mapIterator.hasNext());
        expectedException.expect(NoSuchElementException.class);
        mapIterator.next();
    }

    /**
     * Tests the {@link ru.spbau.kozlov.task03.funclib.prelude.Prelude#filter(ru.spbau.kozlov.task03.funclib.core.Predicate)} function.
     */
    @Test
    public void testFilterPositive() {
        List<Integer> testNumbers = Arrays.asList(4, -8, 15, 16, -23, 42);
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull Integer argument) {
                return argument > 0;
            }
        };

        Iterator<Integer> filterIterator = Prelude.filter(isPositive).apply(testNumbers.iterator());
        assertNotNull("Filter iterator is null", filterIterator);

        for (int testNumber : testNumbers) {
            if (testNumber <= 0) {
                continue;
            }

            assertTrue("Filter iterator is empty", filterIterator.hasNext());
            assertTrue("Filter iterator is empty", filterIterator.hasNext());
            assertEquals("Wrong filtered value", testNumber, (int) filterIterator.next());
        }

        assertFalse("Filter iterator is not empty", filterIterator.hasNext());
        expectedException.expect(NoSuchElementException.class);
        filterIterator.next();
    }

    /**
     * Tests the {@link ru.spbau.kozlov.task03.funclib.prelude.Prelude#filter(ru.spbau.kozlov.task03.funclib.core.Predicate)} function.
     */
    @Test
    public void testFilterNotNegativeNext() {
        List<Integer> testNumbers = Arrays.asList(-4, -8, 15, -16, -23, 42);
        Predicate<Integer> isNotNegative = new Predicate<Integer>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull Integer argument) {
                return argument >= 0;
            }
        };

        Iterator<Integer> filterIterator = Prelude.filter(isNotNegative).apply(testNumbers.iterator());
        assertNotNull("Filter iterator is null", filterIterator);

        assertEquals("Wrong filtered value", 15, (int) filterIterator.next());
        assertEquals("Wrong filtered value", 42, (int) filterIterator.next());

        expectedException.expect(NoSuchElementException.class);
        filterIterator.next();
    }

    /**
     * Tests the {@link ru.spbau.kozlov.task03.funclib.prelude.Prelude#filter(ru.spbau.kozlov.task03.funclib.core.Predicate, java.lang.Iterable)} function.
     */
    @Test
    public void testFilterEmpty() {
        List<Integer> testNumbers = Arrays.asList();
        Iterator<Integer> filterIterator = Prelude.filter(Predicate.alwaysTrue(), testNumbers);
        assertFalse("Filter iterator is not empty", filterIterator.hasNext());
        expectedException.expect(NoSuchElementException.class);
        filterIterator.next();
    }
}
