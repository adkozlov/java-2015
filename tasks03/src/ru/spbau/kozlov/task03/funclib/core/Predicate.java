package ru.spbau.kozlov.task03.funclib.core;

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
    public static final class AlwaysTruePredicate<A> extends Predicate<A> {

        /**
         * Applies the current predicate to the specified value.
         *
         * @param argument the type of a predicate argument
         * @return always {@code true}
         */
        @Override
        public Boolean apply(A argument) {
            return true;
        }

        /**
         * {@inheritDoc}
         *
         * @return
         */
        @Override
        public Predicate<A> not() {
            return alwaysFalse();
        }

        /**
         * {@inheritDoc}
         *
         * @param predicate the specified predicate
         * @return
         */
        @Override
        public Predicate<A> and(final Predicate<? super A> predicate) {
            return new Predicate<A>() {
                @Override
                public Boolean apply(A argument) {
                    return predicate.apply(argument);
                }
            };
        }

        /**
         * {@inheritDoc}
         *
         * @param predicate the specified predicate
         * @return
         */
        @Override
        public Predicate<A> or(Predicate<? super A> predicate) {
            return this;
        }
    }

    /**
     * The predicate that always returns {@code false} value.
     *
     * @param <A> the type of a predicate argument
     */
    public static final class AlwaysFalsePredicate<A> extends Predicate<A> {

        /**
         * Applies the current predicate to the specified value.
         *
         * @param argument the type of a predicate argument
         * @return always {@code false}
         */
        @Override
        public Boolean apply(A argument) {
            return false;
        }

        /**
         * {@inheritDoc}
         *
         * @return
         */
        @Override
        public Predicate<A> not() {
            return alwaysTrue();
        }

        /**
         * {@inheritDoc}
         *
         * @param predicate the specified predicate
         * @return
         */
        @Override
        public Predicate<A> and(Predicate<? super A> predicate) {
            return this;
        }

        /**
         * {@inheritDoc}
         *
         * @param predicate the specified predicate
         * @return
         */
        @Override
        public Predicate<A> or(final Predicate<? super A> predicate) {
            return new Predicate<A>() {
                @Override
                public Boolean apply(A argument) {
                    return predicate.apply(argument);
                }
            };
        }
    }

    /**
     * Applies the current predicate to the specified value.
     *
     * @param argument the type of a predicate argument
     * @return the value of predicate
     */
    @Override
    public abstract Boolean apply(A argument);

    /**
     * Returns a new predicate that is a {@code not} predicate of the current predicate.
     *
     * @return a new instance of a predicate
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#not(ru.spbau.kozlov.task03.funclib.core.Predicate)
     */
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
    public Predicate<A> and(final Predicate<? super A> predicate) {
        return and(this, predicate);
    }

    /**
     * Returns a new instance of a predicate that is an {@code or} predicate of the current predicate and the specified one.
     *
     * @param predicate the specified predicate
     * @return a predicates composition
     * @see ru.spbau.kozlov.task03.funclib.core.Predicate#or(ru.spbau.kozlov.task03.funclib.core.Predicate, ru.spbau.kozlov.task03.funclib.core.Predicate)
     */
    public Predicate<A> or(final Predicate<? super A> predicate) {
        return or(this, predicate);
    }

    /**
     * Returns a new predicate that is a {@code not} predicate of the specified predicate.
     *
     * @param predicate a predicate to be negated
     * @param <A>       the type of a predicate argument
     * @return a new instance of a predicate
     */
    public static <A> Predicate<A> not(final Predicate<? super A> predicate) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A argument) {
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
    public static <A> Predicate<A> and(final Predicate<? super A> firstPredicate, final Predicate<? super A> secondPredicate) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A argument) {
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
    public static <A> Predicate<A> or(final Predicate<? super A> firstPredicate, final Predicate<? super A> secondPredicate) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A argument) {
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
    public static <A> Predicate<A> alwaysFalse() {
        return new AlwaysFalsePredicate<>();
    }

    /**
     * Returns a new instance of a predicate that checks if the argument is {@code null}.
     *
     * @param <A> the type of a predicate argument
     * @return a new instance of a predicate
     */
    public static <A> Predicate<A> notNull() {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A argument) {
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
    public static <A extends Comparable<? super A>> Predicate<A> equals(final A argument) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A comparable) {
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
    public static <A extends Comparable<? super A>> Predicate<A> notEquals(final A argument) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A comparable) {
                return comparable.compareTo(argument) != 0;
            }
        };
    }

    /**
     * Returns a new instance of a predicate that checks if the argument is less than the specified value.
     *
     * @param argument a value to be compared to
     * @param <A>      the type of a predicate argument
     * @return a new instance of a predicate
     */
    public static <A extends Comparable<? super A>> Predicate<A> less(final A argument) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A comparable) {
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
    public static <A extends Comparable<? super A>> Predicate<A> lessOrEquals(final A argument) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A comparable) {
                return comparable.compareTo(argument) <= 0;
            }
        };
    }

    /**
     * Returns a new instance of a predicate that checks if the argument is greater than the specified value.
     *
     * @param argument a value to be compared to
     * @param <A>      the type of a predicate argument
     * @return a new instance of a predicate
     */
    public static <A extends Comparable<? super A>> Predicate<A> greater(final A argument) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A comparable) {
                return comparable.compareTo(argument) > 0;
            }
        };
    }

    /**
     * Returns a new instance of a predicate that checks if the argument is greater or equal to the specified value.
     *
     * @param argument a value to be compared to
     * @param <A>      the type of a predicate argument
     * @return a new instance of a predicate
     */
    public static <A extends Comparable<? super A>> Predicate<A> greaterOrEquals(final A argument) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A comparable) {
                return comparable.compareTo(argument) >= 0;
            }
        };
    }
}
