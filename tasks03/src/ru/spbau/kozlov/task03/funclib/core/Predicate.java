package ru.spbau.kozlov.task03.funclib.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@link ru.spbau.kozlov.task03.funclib.core.Predicate} class represents an abstract predicate.
 *
 * @author adkozlov
 */
public abstract class Predicate<A> extends Function<A, Boolean> {

    /**
     * The predicate that always returns {@code true} value.
     *
     * @param <A> the type of a predicate argument
     */
    private static final class AlwaysTruePredicate<A> extends Predicate<A> {
        /**
         * Applies the current predicate to the specified value.
         *
         * @param argument the type of a predicate argument
         * @return always {@code true}
         */
        @NotNull
        @Override
        public Boolean apply(@NotNull A argument) {
            return true;
        }
    }

    /**
     * The predicate that always returns {@code false} value.
     *
     * @param <A> the type of a predicate argument
     */
    private static final class AlwaysFalsePredicate<A> extends Predicate<A> {
        /**
         * Applies the current predicate to the specified value.
         *
         * @param argument the type of a predicate argument
         * @return always {@code false}
         */
        @NotNull
        @Override
        public Boolean apply(@NotNull A argument) {
            return false;
        }
    }

    /**
     * Applies the current predicate to the specified value.
     *
     * @param argument the type of a predicate argument
     * @return the value of predicate
     */
    @NotNull
    @Override
    public abstract Boolean apply(@NotNull A argument);

    /**
     * Returns a new predicate that is a {@code not} predicate of the current predicate.
     *
     * @return a new instance of a predicate
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#not(ru.spbau.kozlov.task03.funclib.core.Predicate)
     */
    @NotNull
    public Predicate<A> not() {
        return not(this);
    }

    /**
     * Returns a new instance of a predicate that is an {@code and} predicate of the current predicate and the specified one.
     *
     * @param predicate the specified predicate
     * @return a predicates composition
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#and(ru.spbau.kozlov.task03.funclib.core.Predicate, ru.spbau.kozlov.task03.funclib.core.Predicate)
     */
    @NotNull
    public Predicate<A> and(@NotNull final Predicate<A> predicate) {
        return and(this, predicate);
    }

    /**
     * Returns a new instance of a predicate that is an {@code or} predicate of the current predicate and the specified one.
     *
     * @param predicate the specified predicate
     * @return a predicates composition
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#or(ru.spbau.kozlov.task03.funclib.core.Predicate, ru.spbau.kozlov.task03.funclib.core.Predicate)
     */
    @NotNull
    public Predicate<A> or(@NotNull final Predicate<A> predicate) {
        return or(this, predicate);
    }

    /**
     * Returns a new predicate that is a {@code not} predicate of the specified predicate.
     *
     * @param predicate a predicate to be negated
     * @param <A>       the type of a predicate argument
     * @return a new instance of a predicate
     */
    @NotNull
    public static <A> Predicate<A> not(@NotNull final Predicate<A> predicate) {
        if (predicate instanceof AlwaysTruePredicate) {
            return new AlwaysFalsePredicate<>();
        }
        if (predicate instanceof AlwaysFalsePredicate) {
            return new AlwaysTruePredicate<>();
        }
        return new Predicate<A>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull A argument) {
                return !predicate.apply(argument);
            }
        };
    }

    /**
     * Returns a new instance of a predicate that is an {@code and} predicate of two specified predicates.
     *
     * @param firstPredicate  the first predicate
     * @param secondPredicate the second predicate
     * @param <A>             the type of a predicates argument
     * @return a predicates composition
     */
    @NotNull
    public static <A> Predicate<A> and(@NotNull final Predicate<A> firstPredicate, @NotNull final Predicate<A> secondPredicate) {
        if (firstPredicate instanceof AlwaysFalsePredicate || secondPredicate instanceof AlwaysTruePredicate) {
            return firstPredicate;
        }
        if (firstPredicate instanceof AlwaysTruePredicate || secondPredicate instanceof AlwaysFalsePredicate) {
            return secondPredicate;
        }
        return new Predicate<A>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull A argument) {
                return firstPredicate.apply(argument) && secondPredicate.apply(argument);
            }
        };
    }

    /**
     * Returns a new instance of a predicate that is an {@code or} predicate of two specified predicates.
     *
     * @param firstPredicate  the first predicate
     * @param secondPredicate the second predicate
     * @param <A>             the type of a predicates argument
     * @return a predicates composition
     */
    @NotNull
    public static <A> Predicate<A> or(@NotNull final Predicate<A> firstPredicate, @NotNull final Predicate<A> secondPredicate) {
        if (firstPredicate instanceof AlwaysTruePredicate || secondPredicate instanceof AlwaysFalsePredicate) {
            return firstPredicate;
        }
        if (firstPredicate instanceof AlwaysFalsePredicate || secondPredicate instanceof AlwaysTruePredicate) {
            return secondPredicate;
        }
        return new Predicate<A>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull A argument) {
                return firstPredicate.apply(argument) || secondPredicate.apply(argument);
            }
        };
    }

    /**
     * Returns a new instance of a predicate that is always {@code true}.
     *
     * @param <A> the type of a predicate argument
     * @return a new instance of a predicate
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate.AlwaysTruePredicate
     */
    @NotNull
    public static <A> Predicate<A> alwaysTrue() {
        return new AlwaysTruePredicate<>();
    }

    /**
     * Returns a new instance of a predicate that is always {@code false}.
     *
     * @param <A> the type of a predicate argument
     * @return a new instance of a predicate
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate.AlwaysFalsePredicate
     */
    @NotNull
    public static <A> Predicate<A> alwaysFalse() {
        return new AlwaysFalsePredicate<>();
    }

    /**
     * Returns a new instance of a predicate that checks if the argument is {@code null}.
     *
     * @param <A> the type of a predicate argument
     * @return a new instance of a predicate
     */
    @NotNull
    public static <A> Predicate<A> notNull() {
        return new Predicate<A>() {
            @NotNull
            @Override
            public Boolean apply(@Nullable A argument) {
                return argument != null;
            }
        };
    }

    /**
     * Returns a new instance of a predicate that checks if the argument is equal to the specified value.
     *
     * @param argument a value to be compared to
     * @param <A>      the type of a predicate argument
     * @return a new instance of a predicate
     */
    @NotNull
    public static <A extends Comparable<? super A>> Predicate<A> equals(@NotNull final A argument) {
        return new Predicate<A>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull A comparable) {
                return comparable.compareTo(argument) == 0;
            }
        };
    }

    /**
     * Returns a new instance of a predicate that checks if the argument is not equal to the specified value.
     *
     * @param argument a value to be compared to
     * @param <A>      the type of a predicate argument
     * @return a new instance of a predicate
     */
    @NotNull
    public static <A extends Comparable<? super A>> Predicate<A> notEquals(@NotNull final A argument) {
        return equals(argument).not();
    }

    /**
     * Returns a new instance of a predicate that checks if the argument is less than the specified value.
     *
     * @param argument a value to be compared to
     * @param <A>      the type of a predicate argument
     * @return a new instance of a predicate
     */
    @NotNull
    public static <A extends Comparable<? super A>> Predicate<A> less(@NotNull final A argument) {
        return new Predicate<A>() {
            @NotNull
            @Override
            public Boolean apply(@NotNull A comparable) {
                return comparable.compareTo(argument) < 0;
            }
        };
    }

    /**
     * Returns a new instance of a predicate that checks if the argument is less or equal to the specified value.
     *
     * @param argument a value to be compared to
     * @param <A>      the type of a predicate argument
     * @return a new instance of a predicate
     */
    @NotNull
    public static <A extends Comparable<? super A>> Predicate<A> lessOrEquals(@NotNull final A argument) {
        return less(argument).or(equals(argument));
    }

    /**
     * Returns a new instance of a predicate that checks if the argument is greater than the specified value.
     *
     * @param argument a value to be compared to
     * @param <A>      the type of a predicate argument
     * @return a new instance of a predicate
     */
    @NotNull
    public static <A extends Comparable<? super A>> Predicate<A> greater(@NotNull final A argument) {
        return lessOrEquals(argument).not();
    }

    /**
     * Returns a new instance of a predicate that checks if the argument is greater or equal to the specified value.
     *
     * @param argument a value to be compared to
     * @param <A>      the type of a predicate argument
     * @return a new instance of a predicate
     */
    @NotNull
    public static <A extends Comparable<? super A>> Predicate<A> greaterOrEquals(@NotNull final A argument) {
        return less(argument).not();
    }
}
