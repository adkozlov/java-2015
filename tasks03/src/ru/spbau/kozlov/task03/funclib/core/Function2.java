package ru.spbau.kozlov.task03.funclib.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    public abstract C apply(@NotNull A firstArgument, @NotNull B secondArgument);

    /**
     * Returns the current function with the first parameter bind.
     *
     * @param argument an argument to be bind
     * @return the new function of a type B -> C
     */
    @NotNull
    public Function<B, C> bind1(@NotNull final A argument) {
        return new Function<B, C>() {
            @Nullable
            @Override
            public C apply(@NotNull B secondArgument) {
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
    @NotNull
    public Function<A, C> bind2(@NotNull final B argument) {
        return new Function<A, C>() {
            @Nullable
            @Override
            public C apply(@NotNull A firstArgument) {
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
    @NotNull
    public <D> Function2<A, B, D> then(@NotNull final Function<? super C, D> function) {
        return new Function2<A, B, D>() {
            @Nullable
            @Override
            public D apply(@NotNull A firstArgument, @NotNull B secondArgument) {
                return function.apply(Function2.this.apply(firstArgument, secondArgument));
            }
        };
    }
}
