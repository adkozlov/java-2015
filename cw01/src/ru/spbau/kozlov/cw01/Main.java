package ru.spbau.kozlov.cw01;

/**
 * @author adkozlov
 */
public class Main {

    public static void main(String[] args) {
        Polynomial<Number> first = new Polynomial<>();
        System.out.println(first);
        first.add(new Monomial<>(new Number(2d), 2));
        first.add(new Monomial<>(new Number(1d), 1));
        first.add(new Monomial<>(new Number(1d), 0));

        Polynomial<Number> second = new Polynomial<>();
        second.add(new Monomial<>(new Number(3d), 3));
        second.add(new Monomial<>(new Number(2d), 0));

        System.out.println(first.equals(second));
        System.out.println(first.equals(new Polynomial<>(first)));

        Polynomial<Number> result = first.multiply(second);
        System.out.println(result);
        System.out.println(result.eval(new Number(0d)));
        System.out.println(result.eval(new Number(1d)));
        System.out.println(result.eval(new Number(2d)));

        result = new Polynomial<>();
        System.out.println(result);
        System.out.println(result.eval(new Number(1d)));

        System.out.println(Polynomial.ZERO.equals(result));
    }
}
