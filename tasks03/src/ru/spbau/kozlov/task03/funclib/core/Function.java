package ru.spbau.kozlov.task03.funclib.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@link ru.spbau.kozlov.task03.funclib.core.Function} class represents an abstract function of one argument.
 * The function has a type A -> B.
 *
 * @author adkozlov
 */
public abstract class Function<A, B> {

    /**
     * Returns the current functions value calculated with the specified argument.
     *
     * @param argument a function argument of type A
     * @return the value of the function of a type B
     */
    @Nullable
    public abstract B apply(@NotNull A argument);

    /**
     * Returns a composition of two functions (of types A -> B and B -> C).
     * The composition will have a type A -> C.
     *
     * @param function a function to be applied to the result of the current one
     * @param <C>      the return type of the second function
     * @return the composition of two functions
     */
    @NotNull
    public <C> Function<A, C> then(@NotNull final Function<? super B, C> function) {
        return new Function<A, C>() {
            @Nullable
            @Override
            public C apply(@NotNull A argument) {
                return function.apply(Function.this.apply(argument));
            }
        };
    }
}
