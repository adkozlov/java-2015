package ru.spbau.kozlov.task03.funclib.prelude;

import ru.spbau.kozlov.task03.funclib.core.Function;

/**
 * The {@link ru.spbau.kozlov.task03.funclib.prelude.Comparator} implements a comparator based on a function.
 *
 * @author adkozlov
 */
public class Comparator<A, B extends Comparable<? super B>> implements java.util.Comparator<A> {

    private final Function<? super A, ? extends B> comparisonFunction;

    /**
     * Constructs a new comparator base on the specified function.
     *
     * @param comparisonFunction a comparison function to be used
     */
    public Comparator(Function<? super A, ? extends B> comparisonFunction) {
        this.comparisonFunction = comparisonFunction;
    }

    /**
     * Returns the result of a comparison of two specified objects.
     *
     * @param firstArgument  the first object to be compared
     * @param secondArgument the second object to be compared
     * @return a comparison result
     */
    @Override
    public int compare(A firstArgument, A secondArgument) {
        return comparisonFunction.apply(firstArgument).compareTo(comparisonFunction.apply(secondArgument));
    }
}
