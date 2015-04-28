package ru.spbau.kozlov.task04.reflector;

import checkers.nullness.quals.NonNull;

/**
 * The {@link Difference} class represents a message that two members/packages/modifiers list are not equal.
 *
 * @author adkozlov
 */
public class Difference {

    @NonNull
    private final String first;
    @NonNull
    private final String second;
    @NonNull
    private final String format;

    /**
     * Constructs a new instance.
     *
     * @param first a string representation of the first member/package/modifiers list
     * @param second a string representation of the second member/package/modifiers list
     * @param format a message format
     */
    public Difference(@NonNull String first, @NonNull String second, @NonNull String format) {
        this.first = first;
        this.second = second;
        this.format = format;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Difference)) return false;

        Difference that = (Difference) o;

        return ((first.equals(that.first) && second.equals(that.second)) || (first.equals(that.second) && second.equals(that.first))) && format.equals(that.format);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        result = 31 * result + format.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(format, first, second);
    }
}
