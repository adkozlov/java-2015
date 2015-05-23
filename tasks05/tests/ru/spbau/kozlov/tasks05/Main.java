package ru.spbau.kozlov.tasks05;

import checkers.nullness.quals.NonNull;
import ru.spbau.kozlov.task05.DistributedSerializator;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author adkozlov
 */
public class Main {

    public static void main(@NonNull String[] args) {
        DistributedSerializator<Student> distributedSerializator = new DistributedSerializator<>();
        String name = "andrew-kozlov";
        int sleepTime = 60000;

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Student student = new Student("Andrew", "Kozlov", random.nextInt(21), 'B', true);
                Future<Boolean> serialization = distributedSerializator.serialize(student, name);
                try {
                    Thread.sleep(random.nextInt(sleepTime));
                    System.out.println(serialization.get() ? "successfully serialized: " + student : "serialization failed");
                } catch (InterruptedException | ExecutionException e) {
                    handleException(e, "serialization");
                }
            }).start();
        }

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Future<Student> deserialization = distributedSerializator.deserialize(name);
                try {
                    Thread.sleep(random.nextInt(sleepTime));
                    System.out.println("deserialized: " + deserialization.get());
                } catch (InterruptedException | ExecutionException e) {
                    handleException(e, "deserialization");
                }
            }).start();
        }

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(random.nextInt(sleepTime));
                    System.out.println(distributedSerializator.get(name));
                } catch (InterruptedException | ExecutionException e) {
                    handleException(e, "get");
                }
            }).start();
        }
    }

    private static void handleException(@NonNull Exception exception, @NonNull String message) {
        System.err.printf("%s failed. message: %s, class: %s\n", message, exception.getMessage(), exception.getClass());
    }
}
