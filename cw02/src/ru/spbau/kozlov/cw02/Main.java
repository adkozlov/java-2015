package ru.spbau.kozlov.cw02;

import java.io.File;
import java.io.IOException;

/**
 * @author adkozlov
 */
public class Main {

    public static void main(String[] args) {
        try {
            new FactoryLoader().loadFactories(new File("."));

            Factory factory = Factory.getInstance();
            System.out.println(factory.getParameters("Pair"));
            System.out.println(factory.createInstance("Pair", "Surname"));
            System.out.println(factory.createInstance("Pair"));

            try {
                factory.createInstance("Pair", new Object());
            } catch (ClassNotFoundException e) {
                System.out.println("correct: " + e.getMessage());
            }

            Builder builder = new Builder();
            System.out.println(builder.createInstance("Pair"));

            try {
                System.out.println(builder.createInstance("Object"));
            } catch (ClassNotFoundException e) {
                System.out.println("correct: " + e.getMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
