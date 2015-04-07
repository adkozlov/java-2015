package ru.spbau.kozlov.cw01;

import java.util.*;

/**
 * @author adkozlov
 */
public class Polynomial<R extends Ring<R>> extends AbstractCollection<Monomial<? extends R>> implements Ring<Polynomial<? extends R>> {

    public static final Polynomial ZERO = new Polynomial();

    private SortedMap<Integer, List<Monomial<? extends R>>> monomialsMap = new TreeMap<>();

    public Polynomial() {
    }

    public Polynomial(R coefficient, int power) {
        this(new Monomial<>(coefficient, power));
    }

    public Polynomial(Monomial<? extends R> monomial) {
        List<Monomial<? extends R>> monomials = new ArrayList<>();
        monomials.add(monomial);

        monomialsMap.put(monomial.getPower(), monomials);
    }

    public Polynomial(Polynomial<R> polynomial) {
        for (Map.Entry<Integer, List<Monomial<? extends R>>> entry : polynomial.monomialsMap.entrySet()) {
            List<Monomial<? extends R>> newMonomials = new ArrayList<>();
            newMonomials.addAll(entry.getValue());

            monomialsMap.put(entry.getKey(), newMonomials);
        }
    }

    @Override
    public Polynomial<R> sum(Polynomial<? extends R> argument) {
        Polynomial<R> result = new Polynomial<>(this);
        result.addAll(argument);
        return result;
    }

    @Override
    public Polynomial<R> multiply(Polynomial<? extends R> argument) {
        Polynomial<R> result = new Polynomial<>();
        for (Monomial<? extends R> thisMonomial : this) {
            for (Monomial<? extends R> thatMonomial : argument) {
                result.add(new Monomial<>(thisMonomial.getCoefficient().multiply(thatMonomial.getCoefficient()),
                        thisMonomial.getPower() + thatMonomial.getPower()));
            }
        }
        return result;
    }

    @Override
    public Iterator<Monomial<? extends R>> iterator() {
        return new Iterator<Monomial<? extends R>>() {
            private final Iterator<Map.Entry<Integer, List<Monomial<? extends R>>>> iterator = monomialsMap.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Monomial<? extends R> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Iterator is empty");
                }

                List<Monomial<? extends R>> monomials = iterator.next().getValue();
                Iterator<Monomial<? extends R>> monomialsIterator = monomials.iterator();

                Monomial<? extends R> monomial = monomialsIterator.next();
                R coefficient = monomial.getCoefficient();
                int power = monomial.getPower();

                while (monomialsIterator.hasNext()) {
                    coefficient = coefficient.sum(monomialsIterator.next().getCoefficient());
                }

                return new Monomial<>(coefficient, power);
            }
        };
    }

    @Override
    public int size() {
        return monomialsMap.isEmpty() ? 0 : monomialsMap.lastKey();
    }

    @Override
    public boolean add(Monomial<? extends R> monomial) {
        monomialsMap.putIfAbsent(monomial.getPower(), new ArrayList<>());
        return monomialsMap.get(monomial.getPower()).add(monomial);
    }

    public R eval(R argument) {
        R result = null;
        for (Monomial<? extends R> monomial : this) {
            int power = monomial.getPower();
            R value;
            if (power != 0) {
                value = power(argument, power);
                value = value.multiply(monomial.getCoefficient());
            } else {
                value = monomial.getCoefficient();
            }

            result = result != null ? result.sum(value) : value;
        }
        return result;
    }

    private R power(R argument, int power) {
        if (power <= 0) {
            throw new IllegalArgumentException("Power should be a positive number");
        }

        R result = null;
        while (power != 0) {
            if ((power & 1) != 0) {
                result = result != null ? result.multiply(argument) : argument;
            }

            argument = argument.multiply(argument);
            power >>= 1;
        }
        return result;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "0_R";
        }

        StringBuilder builder = new StringBuilder();
        List<Monomial<? extends R>> monomials = new ArrayList<>();
        monomials.addAll(this);

        for (ListIterator<Monomial<? extends R>> iterator = monomials.listIterator(monomials.size()); iterator.hasPrevious(); ) {
            Monomial<? extends R> monomial = iterator.previous();

            builder.append("(");
            builder.append(monomial.getCoefficient());
            builder.append(")");
            if (iterator.hasPrevious()) {
                builder.append(" x^");
                builder.append(monomial.getPower());
            }
            if (iterator.hasPrevious()) {
                builder.append(" + ");
            }
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Polynomial)) return false;

        Polynomial that = (Polynomial) o;
        if (that == ZERO) return true;

        Iterator<Monomial<? extends R>> thisIterator = iterator();
        Iterator thatIterator = that.iterator();
        while (thisIterator.hasNext() && thatIterator.hasNext()) {
            if (!thisIterator.next().equals(thatIterator.next())) {
                return false;
            }
        }
        return !thisIterator.hasNext() && !thatIterator.hasNext();
    }

    @Override
    public int hashCode() {
        return monomialsMap != null ? monomialsMap.hashCode() : 0;
    }
}
