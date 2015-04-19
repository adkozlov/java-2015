package ru.spbau.kozlov.cw01;

/**
 * @author adkozlov
 */
public final class Monomial<R extends Ring<R>> implements Multipliable<Monomial<? extends R>> {

    private final R coefficient;
    private final int power;

    public Monomial(R coefficient, int power) {
        if (power < 0) {
            throw new IllegalArgumentException("Power shouldn't be a negative number");
        }

        this.coefficient = coefficient;
        this.power = power;
    }

    public R getCoefficient() {
        return coefficient;
    }

    public int getPower() {
        return power;
    }

    @Override
    public Monomial<R> multiply(Monomial<? extends R> argument) {
        return new Monomial<>(coefficient.multiply(argument.coefficient), power + argument.power);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Monomial)) return false;

        Monomial<?> monomial = (Monomial<?>) o;
        return power == monomial.power && coefficient.equals(monomial.coefficient);

    }

    @Override
    public int hashCode() {
        int result = coefficient.hashCode();
        result = 31 * result + power;
        return result;
    }

    @Override
    public String toString() {
        return "Monomial{" +
                "coefficient=" + coefficient +
                ", power=" + power +
                '}';
    }
}
