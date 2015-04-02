package ru.spbau.kozlov.task03.funclib.core;

/**
 * The {@link ru.spbau.kozlov.task03.funclib.core.Function2} class represents an abstract function of two arguments.
 * The function has a type A -> B -> C.
 *
 * @author adkozlov
 */
public abstract class Function2<A, B, C> {

    /**
     * Returns the current function value calculated with the specified parameters.
     *
     * @param firstArgument  a function argument of type A
     * @param secondArgument a function argument of type B
     * @return the value of the function of a type C
     */
    public abstract C apply(A firstArgument, B secondArgument);

    /**
     * Returns the current function with the first parameter bind.
     *
     * @param argument an argument to be bind
     * @return the new function of a type B -> C
     */
    public Function<B, C> bind1(final A argument) {
        return new Function<B, C>() {
            @Override
            public C apply(B secondArgument) {
                return Function2.this.apply(argument, secondArgument);
            }
        };
    }

    /**
     * Returns the current function with the second parameter bind.
     *
     * @param argument an argument to be bind
     * @return the new function of a type A -> C
     */
    public Function<A, C> bind2(final B argument) {
        return new Function<A, C>() {
            @Override
            public C apply(A firstArgument) {
                return Function2.this.apply(firstArgument, argument);
            }
        };
    }

    /**
     * Returns a composition of two functions (of types A -> B -> C and B -> C -> D).
     * The composition will have a type A -> B -> D.
     *
     * @param function a function to be applied to the result of the current one
     * @param <D>      the return type of the second function
     * @return the composition of two functions of type
     */
    public <D> Function2<A, B, D> then(final Function<? super C, D> function) {
        return new Function2<A, B, D>() {
            @Override
            public D apply(A firstArgument, B secondArgument) {
                return function.apply(Function2.this.apply(firstArgument, secondArgument));
            }
        };
    }
}
