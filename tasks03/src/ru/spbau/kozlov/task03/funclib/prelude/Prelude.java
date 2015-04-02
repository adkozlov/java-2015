package ru.spbau.kozlov.task03.funclib.prelude;

import ru.spbau.kozlov.task03.funclib.core.Function;
import ru.spbau.kozlov.task03.funclib.core.Function2;
import ru.spbau.kozlov.task03.funclib.core.Predicate;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * The {@link ru.spbau.kozlov.task03.funclib.prelude.Prelude} class contains some useful second order functions such as {@code foldr}, {@code take}, {@code map}, {@code filter}.
 *
 * @author adkozlov
 */
public final class Prelude {

    private Prelude() {
    }

    /**
     * Returns an instance of the {@code foldr} function with the first parameter bind with the specified function.
     *
     * @param function a function of a type A -> B to be bind as an argument
     * @param <A>      the parameter type of the specified function
     * @param <B>      the return type of the specified function
     * @return an instance of the {@code foldr} function
     */
    public static <A, B> Function2<? super B, ListIterator<? extends A>, B> foldr(final Function2<? super A, ? super B, B> function) {
        return new Function2<B, ListIterator<? extends A>, B>() {
            @Override
            public B apply(B value, ListIterator<? extends A> iterator) {
                return foldr(function, value, iterator);
            }
        };
    }

    /**
     * Folds right the specified sequence of elements with the specified function and initial value.
     *
     * @param function a function of a type A -> B to be applied to an element
     * @param value    an initial value
     * @param list     a list to be applied to
     * @param <A>      the parameter type of the specified function
     * @param <B>      the return type of the specified function
     * @return a value of a type B
     */
    public static <A, B> B foldr(Function2<? super A, ? super B, B> function, B value, List<? extends A> list) {
        return foldr(function, value, list.listIterator(list.size()));
    }

    private static <A, B> B foldr(Function2<? super A, ? super B, B> function, B value, ListIterator<? extends A> iterator) {
        B result = value;
        while (iterator.hasPrevious()) {
            result = function.apply(iterator.previous(), result);
        }
        return result;
    }

    /**
     * Returns an instance of the {@code take} function with the first parameter bind with the specified count of elements.
     *
     * @param count a number of elements to be taken from the specified sequence
     * @param clazz a type of the elements in the sequence
     * @param <A>   the type of the elements in the sequence
     * @return an instance of the {@code take} function
     */
    public static <A> Function<Iterator<? extends A>, Iterator<A>> take(final int count, Class<A> clazz) {
        return new Function<Iterator<? extends A>, Iterator<A>>() {
            @Override
            public Iterator<A> apply(Iterator<? extends A> iterator) {
                return take(count, iterator);
            }
        };
    }

    /**
     * Takes the specified count of elements from the specified sequence.
     *
     * @param count    a number of elements to be taken from the specified sequence
     * @param iterable a sequence of elements
     * @param <A>      the type of the elements in the sequence
     * @return an iterator to a sequence of taken elements
     */
    public static <A> Iterator<A> take(final int count, final Iterable<? extends A> iterable) {
        return take(count, iterable.iterator());
    }

    private static <A> Iterator<A> take(final int count, final Iterator<? extends A> iterator) {
        return new Iterator<A>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext() && index < count;
            }

            @Override
            public A next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Iterator is empty");
                }
                index++;
                return iterator.next();
            }
        };
    }

    /**
     * Returns an instance of the {@code map} function with the first parameter bind with the specified function.
     *
     * @param function a function of a type A -> B to be applied
     * @param <A>      the parameter type of the specified function
     * @param <B>      the return type of the specified function
     * @return an instance of the {@code map} function
     */
    public static <A, B> Function<Iterator<? extends A>, Iterator<B>> map(final Function<? super A, ? extends B> function) {
        return new Function<Iterator<? extends A>, Iterator<B>>() {
            @Override
            public Iterator<B> apply(Iterator<? extends A> iterator) {
                return map(function, iterator);
            }
        };
    }

    /**
     * Maps the specified function to the specified sequence of elements.
     *
     * @param function a function of a type A -> B to be applied to an element
     * @param iterable a sequence of elements
     * @param <A>      the type of the elements in the specified sequence and the parameter type of the specified function
     * @param <B>      the type of elements to be calculated and the return type of the specified function
     * @return an iterator to a sequence of calculated elements
     */
    public static <A, B> Iterator<B> map(final Function<? super A, ? extends B> function, final Iterable<A> iterable) {
        return map(function, iterable.iterator());
    }

    private static <A, B> Iterator<B> map(final Function<? super A, ? extends B> function, final Iterator<A> iterator) {
        return new Iterator<B>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public B next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Iterator is empty");
                }
                return function.apply(iterator.next());
            }
        };
    }

    /**
     * Returns an instance of the {@code filter} function with the first parameter bind with the specified predicate.
     *
     * @param predicate a predicate to be bind as an argument
     * @param <A>       the parameter type of the specified predicate
     * @return an instance of the {@code filter} function
     */
    public static <A> Function<Iterator<A>, Iterator<A>> filter(final Predicate<? super A> predicate) {
        return new Function<Iterator<A>, Iterator<A>>() {
            @Override
            public Iterator<A> apply(Iterator<A> iterator) {
                return filter(predicate, iterator);
            }
        };
    }

    /**
     * Filters elements that do not match the specified predicate out of the specified sequence.
     *
     * @param predicate a predicate to be applied to an element
     * @param iterable  a sequence of elements
     * @param <A>       the type of elements in the specified sequence
     * @return an iterator to a sequence of elements that match the specified predicate
     */
    public static <A> Iterator<A> filter(final Predicate<? super A> predicate, final Iterable<A> iterable) {
        return filter(predicate, iterable.iterator());
    }

    private static <A> Iterator<A> filter(final Predicate<? super A> predicate, final Iterator<A> iterator) {
        return new Iterator<A>() {
            private A cached;

            @Override
            public boolean hasNext() {
                if (cached != null) {
                    return true;
                }
                while (iterator.hasNext()) {
                    A value = iterator.next();
                    if (predicate.apply(value)) {
                        cached = value;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public A next() {
                if (cached != null) {
                    return pollCached();
                }
                if (!hasNext()) {
                    throw new NoSuchElementException("Iterator is empty");
                }
                return pollCached();
            }

            private A pollCached() {
                A result = cached;
                cached = null;
                return result;
            }
        };
    }
}
