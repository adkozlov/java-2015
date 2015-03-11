package ru.spbau.kozlov.task03.tests;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import ru.spbau.kozlov.task03.funclib.core.Predicate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The {@link TestPredicates} class tests the methods of the {@link ru.spbau.kozlov.task03.funclib.core.Predicate} class.
 *
 * @author adkozlov
 */
public class TestPredicates {

    /**
     * Tests {@link ru.spbau.kozlov.task03.funclib.core.Predicate#alwaysTrue()} and {@link ru.spbau.kozlov.task03.funclib.core.Predicate#alwaysFalse()} predicates.
     * <p/>
     * * @see ru.spbau.kozlov.task03.funclib.core.Predicate#not(ru.spbau.kozlov.task03.funclib.core.Predicate)
     */
    @Test
    public void testAlwaysPredicates() {
        List<Integer> testNumbers = Arrays.asList(4, 8, 15, 16, 23, 42);
        Predicate<Integer> alwaysTrue = Predicate.alwaysTrue();
        Predicate<Integer> alwaysNotTrue = Predicate.not(alwaysTrue);
        Predicate<Integer> alwaysFalse = Predicate.alwaysFalse();
        Predicate<Integer> alwaysNotFalse = Predicate.not(alwaysFalse);

        for (int number : testNumbers) {
            assertTrue("Wrong always true predicate value", alwaysTrue.apply(number));
            assertFalse("Wrong always false predicate value", alwaysNotTrue.apply(number));
            assertFalse("Wrong always false predicate value", alwaysFalse.apply(number));
            assertTrue("Wrong always true predicate value", alwaysNotFalse.apply(number));
        }
    }

    /**
     * Tests logical predicates.
     *
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#not
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#or
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#and
     */
    @Test
    public void testLogicalPredicates() {
        List<Integer> testNumbers = Arrays.asList(4, 8, -15, 16, -23, 42);
        Predicate<Integer> absIsGreaterThan15 = new Predicate<Integer>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull Integer argument) {
                return argument > 15;
            }
        }.or(new Predicate<Integer>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull Integer argument) {
                return argument < -15;
            }
        });
        Predicate<Integer> absIsLessThan16 = new Predicate<Integer>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull Integer argument) {
                return argument < 16;
            }
        }.and(new Predicate<Integer>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull Integer argument) {
                return argument > -16;
            }
        });
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull Integer argument) {
                return argument <= 0;
            }
        }.not();

        for (int number : testNumbers) {
            assertEquals("Wrong or predicate value", Math.abs(number) > 15, absIsGreaterThan15.apply(number));
            assertEquals("Wrong and predicate value", Math.abs(number) < 16, absIsLessThan16.apply(number));
            assertEquals("Wrong not predicate value", number > 0, isPositive.apply(number));
        }
    }

    /**
     * Tests logical operations on {@link ru.spbau.kozlov.task03.funclib.core.Predicate#alwaysTrue()} and {@link ru.spbau.kozlov.task03.funclib.core.Predicate#alwaysFalse} predicates.
     * <p/>
     * * @see ru.spbau.kozlov.task03.funclib.core.Predicate#or(ru.spbau.kozlov.task03.funclib.core.Predicate, ru.spbau.kozlov.task03.funclib.core.Predicate)
     * * @see ru.spbau.kozlov.task03.funclib.core.Predicate#and(ru.spbau.kozlov.task03.funclib.core.Predicate, ru.spbau.kozlov.task03.funclib.core.Predicate)
     */
    @Test
    public void testAlwaysPredicatesCompositions() {
        List<Integer> testNumbers = Arrays.asList(4, 8, -15, 16, -23, 42);
        Predicate<Integer> alwaysTrue = Predicate.alwaysTrue();
        Predicate<Integer> alwaysFalse = Predicate.alwaysFalse();

        Predicate<Integer> trueOrFalsePredicate = Predicate.or(alwaysTrue, alwaysFalse);
        Predicate<Integer> falseOrTruePredicate = Predicate.or(alwaysFalse, alwaysTrue);
        Predicate<Integer> trueAndFalsePredicate = Predicate.and(alwaysTrue, alwaysFalse);
        Predicate<Integer> falseAndTruePredicate = Predicate.and(alwaysFalse, alwaysTrue);

        for (int number : testNumbers) {
            assertTrue("Wrong or predicate value", trueOrFalsePredicate.apply(number));
            assertTrue("Wrong or predicate value", falseOrTruePredicate.apply(number));
            assertFalse("Wrong and predicate value", trueAndFalsePredicate.apply(number));
            assertFalse("Wrong and predicate value", falseAndTruePredicate.apply(number));
        }
    }

    /**
     * Tests the {@link ru.spbau.kozlov.task03.funclib.core.Predicate#notNull} predicate.
     */
    @Test
    public void testNotNull() {
        Object object = new Object();
        Predicate<Object> notNull = Predicate.notNull();
        assertTrue("Wrong not null predicate value", notNull.apply(object));

        object = null;
        assertFalse("Wrong not null predicate value", notNull.apply(object));
    }

    /**
     * Tests comparison predicates.
     *
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#equals
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#notEquals
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#less
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#lessOrEquals
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#greater
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#greaterOrEquals
     */
    @Test
    public void testComparisonPredicates() {
        List<Integer> testNumbers = Arrays.asList(4, 8, 15, 16, 23, 42);
        Predicate<Integer> equals4 = Predicate.equals(4);
        Predicate<Integer> notEquals8 = Predicate.notEquals(8);
        Predicate<Integer> lessThan15 = Predicate.less(15);
        Predicate<Integer> lessOrEquals16 = Predicate.lessOrEquals(16);
        Predicate<Integer> greaterThan23 = Predicate.greater(23);
        Predicate<Integer> greaterOrEquals42 = Predicate.greaterOrEquals(42);

        for (int number : testNumbers) {
            assertEquals("Wrong equals predicate value", number == 4, equals4.apply(number));
            assertEquals("Wrong not equals predicate value", number != 8, notEquals8.apply(number));
            assertEquals("Wrong less predicate value", number < 15, lessThan15.apply(number));
            assertEquals("Wrong less or equals predicate value", number <= 16, lessOrEquals16.apply(number));
            assertEquals("Wrong greater predicate value", number > 23, greaterThan23.apply(number));
            assertEquals("Wrong greater or equals predicate value", number >= 42, greaterOrEquals42.apply(number));
        }
    }
}
