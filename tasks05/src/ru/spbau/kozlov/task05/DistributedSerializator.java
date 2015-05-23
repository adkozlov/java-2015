package ru.spbau.kozlov.task05;

import checkers.nullness.quals.NonNull;
import checkers.nullness.quals.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author adkozlov
 */
public class DistributedSerializator<T> {

    private static final int THREADS_COUNT = 10;

    @NonNull
    private static final String PROPERTIES_FILE_EXTENSION = ".properties";
    @NonNull
    public static final String CANONICAL_NAME_KEY = "canonicalName";
    @NonNull
    private static final String GETTER_PREFIX = "get";
    @NonNull
    private static final String SETTER_PREFIX = "set";

    @NonNull
    private static final Class<?>[] CLASSES = new Class<?>[]{
            String.class,
            Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
            Float.TYPE, Double.TYPE,
            Boolean.TYPE, Character.TYPE
    };

    @NonNull
    private final ExecutorService service;
    @NonNull
    private final AtomicBoolean isTaskAdded = new AtomicBoolean(false);
    @NonNull
    private final ConcurrentMap<String, T> values = new ConcurrentHashMap<>();
    @NonNull
    private final ConcurrentMap<String, ConditionLock> deserializationLocks = new ConcurrentHashMap<>();
    @NonNull
    private final ConcurrentMap<Path, ConcurrentMap<ConditionLock, Boolean>> readLocks = new ConcurrentHashMap<>();

    public DistributedSerializator() {
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        service = new ThreadPoolExecutor(THREADS_COUNT + 1, THREADS_COUNT + 1, 0L, TimeUnit.MILLISECONDS, queue);

        service.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (!isTaskAdded.get() || !queue.isEmpty()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // ignoring
                        }
                    }

                    synchronized (queue) {
                        if (queue.isEmpty()) {
                            service.shutdownNow();
                            break;
                        }
                    }
                }
            }
        });
    }

    @NonNull
    public Future<Boolean> serialize(@NonNull T object, @NonNull String name) {
        isTaskAdded.set(true);
        return service.submit(() -> {
            Properties properties = new Properties();
            properties.setProperty(CANONICAL_NAME_KEY, object.getClass().getCanonicalName());

            for (Method getter : Arrays.asList(object.getClass().getDeclaredMethods())
                    .stream()
                    .filter(m -> m.getName().startsWith(GETTER_PREFIX))
                    .collect(Collectors.toList())) {
                try {
                    properties.setProperty(getter.getName().replace(GETTER_PREFIX, ""), getter.invoke(object).toString());
                } catch (IllegalAccessException e) {
                    // ignoring
                }
            }

            Path tempFilePath = Files.createTempFile("", "");
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(tempFilePath)) {
                properties.store(bufferedWriter, null);
            }

            Path path = Paths.get(getPropertiesFileName(name));
            ConcurrentMap<ConditionLock, Boolean> locks = getReadLocks(path);
            synchronized (locks) {
                for (Map.Entry<ConditionLock, Boolean> entry : locks.entrySet()) {
                    ConditionLock lock = entry.getKey();
                    synchronized (lock) {
                        lock.await();
                    }
                }

                if (!Thread.currentThread().isInterrupted()) {
                    Files.move(tempFilePath, path, StandardCopyOption.ATOMIC_MOVE);
                } else {
                    Files.delete(tempFilePath);
                    return false;
                }
            }

            return true;
        });
    }

    public Future<T> deserialize(@NonNull String name) {
        isTaskAdded.set(true);
        return service.submit(() -> {
            Properties properties = new Properties();
            String fileName = getPropertiesFileName(name);

            Path path = Paths.get(fileName);
            ConcurrentMap<ConditionLock, Boolean> locks = getReadLocks(path);

            ConditionLock lock = null;
            try {
                lock = new ConditionLock();
                lock.lock();
                locks.put(lock, true);

                try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
                    properties.load(bufferedReader);
                }
            } finally {
                if (lock != null) {
                    synchronized (lock) {
                        lock.signal();
                    }
                    locks.remove(lock);
                    synchronized (lock) {
                        lock.unlock();
                    }
                }
            }

            String canonicalName = properties.getProperty(CANONICAL_NAME_KEY);
            if (canonicalName == null) {
                throw new RuntimeException(String.format("Properties file \'%s\' doesn't contain required property: %s", fileName, CANONICAL_NAME_KEY));
            }

            Class<?> clazz = Class.forName(canonicalName);
            T result = (T) clazz.newInstance();
            for (String propertyName : properties.stringPropertyNames()) {
                Method setter = findSetterByName(propertyName, clazz);
                if (setter != null) {
                    try {
                        setter.invoke(result, stringToObject(properties.getProperty(propertyName), setter.getParameterTypes()[0]));
                    } catch (IllegalAccessException e) {
                        // ignoring
                    }
                }
            }

            values.put(name, result);
            return result;
        });
    }

    private ConcurrentMap<ConditionLock, Boolean> getReadLocks(Path path) {
        readLocks.putIfAbsent(path, new ConcurrentHashMap<>());
        return readLocks.get(path);
    }

    @NonNull
    public T get(@NonNull String name) throws ExecutionException, InterruptedException {
        T result = values.get(name);
        if (result != null) {
            return result;
        }

        ConditionLock lock = null;
        try {
            synchronized (deserializationLocks) {
                lock = deserializationLocks.get(name);
                if (lock == null) {
                    synchronized (values) {
                        result = values.get(name);
                        if (result != null) {
                            return result;
                        }

                        lock = new ConditionLock();
                        lock.lock();
                        deserializationLocks.put(name, lock);
                    }
                }
            }

            if (!lock.isHeldByCurrentThread()) {
                lock.await();
                return values.get(name);
            }

            return deserialize(name).get();
        } finally {
            synchronized (deserializationLocks) {
                if (lock != null && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    lock.signalAll();
                    deserializationLocks.remove(name);
                }
            }
        }
    }

    @NonNull
    public static String getPropertiesFileName(@NonNull String name) {
        return name + PROPERTIES_FILE_EXTENSION;
    }

    @Nullable
    private static Method findSetterByName(@NonNull String name, @NonNull Class<?> clazz) {
        String setterName = SETTER_PREFIX + name;
        for (Class<?> parameterClass : CLASSES) {
            try {
                Method setter = clazz.getDeclaredMethod(setterName, parameterClass);
                if (setter.getParameterCount() == 1) {
                    return setter;
                }
            } catch (NoSuchMethodException e) {
                // ignoring
            }
        }
        return null;
    }

    @NonNull
    private static Object stringToObject(@NonNull String string, @NonNull Class<?> clazz) {
        Class<?> wrapperClass = Array.get(Array.newInstance(clazz, 1), 0).getClass();
        Optional<Constructor<?>> fromStringConstructor = Arrays.asList(wrapperClass.getDeclaredConstructors())
                .stream()
                .filter(c -> c.getParameterCount() == 1 && c.getParameterTypes()[0] == String.class)
                .findFirst();
        try {
            return fromStringConstructor.isPresent() ?
                    fromStringConstructor.get().newInstance(string) : wrapperClass.getConstructor(char.class).newInstance(string.charAt(0));
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            // cannot be
        }
        // cannot be
        return null;
    }

    private static class ConditionLock extends ReentrantLock implements Condition {

        @NonNull
        private final Condition condition;

        private ConditionLock() {
            condition = newCondition();
        }

        @Override
        public void await() throws InterruptedException {
            condition.await();
        }

        @Override
        public void awaitUninterruptibly() {
            condition.awaitUninterruptibly();
        }

        @Override
        public long awaitNanos(long nanosTimeout) throws InterruptedException {
            return condition.awaitNanos(nanosTimeout);
        }

        @Override
        public boolean await(long time, TimeUnit unit) throws InterruptedException {
            return condition.await(time, unit);
        }

        @Override
        public boolean awaitUntil(Date deadline) throws InterruptedException {
            return condition.awaitUntil(deadline);
        }

        @Override
        public void signal() {
            condition.signal();
        }

        @Override
        public void signalAll() {
            condition.signalAll();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (!(o instanceof ConditionLock)) return false;

            ConditionLock that = (ConditionLock) o;
            return condition.equals(that.condition);
        }

        @Override
        public int hashCode() {
            return condition.hashCode();
        }
    }
}
