package ru.spbau.kozlov.cw01;

/**
 * @author adkozlov
 */
public final class Number implements Ring<Number> {

    private final Double value;

    public Number() {
        this(0d);
    }

    public Number(Double value) {
        this.value = value;
    }

    public Number(Number value) {
        this(value.value);
    }

    @Override
    public Number sum(Number argument) {
        return new Number(value + argument.value);
    }

    @Override
    public Number multiply(Number argument) {
        return new Number(value * argument.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Number)) return false;

        Number number = (Number) o;
        return Double.compare(number.value, value) == 0;

    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
