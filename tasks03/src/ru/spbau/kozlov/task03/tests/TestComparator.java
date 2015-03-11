package ru.spbau.kozlov.task03.tests;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import ru.spbau.kozlov.task03.funclib.core.Function;
import ru.spbau.kozlov.task03.funclib.prelude.Comparator;

import static org.junit.Assert.assertTrue;

/**
 * The {@link TestComparator} class contains a test of comparison with the {@link ru.spbau.kozlov.task03.funclib.prelude.Comparator}.
 *
 * @author adkozlov
 */
public class TestComparator {

    /**
     * Tests then {@link ru.spbau.kozlov.task03.funclib.prelude.Comparator#compare} function.
     */
    @Test
    public void testComparator() {
        Comparator<Integer, Integer> absComparator = new Comparator<>(new Function<Integer, Integer>() {
            @NotNull
            @Override
            public Integer apply(@NotNull Integer argument) {
                return Math.abs(argument);
            }
        });

        assertTrue("Wrong comparison result", absComparator.compare(-42, 23) > 0);
    }
}
