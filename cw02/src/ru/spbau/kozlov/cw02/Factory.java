package ru.spbau.kozlov.cw02;

import checkers.nullness.quals.NonNull;
import checkers.nullness.quals.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author adkozlov
 */
public class Factory {

    private static final Factory INSTANCE = new Factory();

    @NonNull
    private final Map<String, ArrayList<ConcreteFactory>> factories = new HashMap<>();

    private Factory() {
    }

    public void registerConcreteFactory(@NonNull ConcreteFactory factory, @NonNull String canonicalName) {
        factories.putIfAbsent(canonicalName, new ArrayList<>());
        factories.get(canonicalName).add(factory);
    }

    @Nullable
    public Object createInstance(@NonNull String canonicalName, @NonNull Object... args) throws ClassNotFoundException {
        ArrayList<ConcreteFactory> factoryList = getFactories(canonicalName);
        ArrayList<ArrayList<Class<?>>> parameters = getParameters(canonicalName);

        boolean canReturnNull = false;
        for (int i = 0; i < parameters.size(); i++) {
            ArrayList<Class<?>> constructorParameters = parameters.get(i);
            if (constructorParameters.size() == args.length) {
                try {
                    Method createInstance = factoryList.get(i).getClass().getDeclaredMethod("createInstance", getParametersClasses(args));
                    return createInstance.invoke(factoryList.get(i), args);
                } catch (NoSuchMethodException | IllegalAccessException e) {
                    // ignoring
                } catch (InvocationTargetException e) {
                    canReturnNull = true;
                }
            }
        }

        if (canReturnNull) {
            return null;
        }
        throw new ClassNotFoundException("Instance cannot be created with specified arguments types: " + Arrays.toString(getParametersClasses(args)));
    }

    @NonNull
    public ArrayList<ArrayList<Class<?>>> getParameters(@NonNull String canonicalName) throws ClassNotFoundException {
        ArrayList<ArrayList<Class<?>>> result = new ArrayList<>();
        for (ConcreteFactory factory : getFactories(canonicalName)) {
            result.add(asArrayList(Arrays.asList(factory.getClass().getDeclaredMethods())
                    .stream()
                    .filter(method -> method.getName().equals("createInstance"))
                    .findFirst()
                    .get()
                    .getParameterTypes()));
        }
        return result;
    }

    @NonNull
    private ArrayList<ConcreteFactory> getFactories(@NonNull String canonicalName) throws ClassNotFoundException {
        ArrayList<ConcreteFactory> factoryList = factories.get(canonicalName);
        if (factoryList == null) {
            throw getClassNotFoundException(canonicalName);
        }
        return factoryList;
    }

    @NonNull
    private static ClassNotFoundException getClassNotFoundException(@NonNull String canonicalName) {
        return new ClassNotFoundException("No factory is registered for class: " + canonicalName);
    }

    @NonNull
    private static Class<?>[] getParametersClasses(@NonNull Object... args) {
        Class<?>[] result = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = args[i].getClass();
        }
        return result;
    }

    @NonNull
    private static ArrayList<Class<?>> asArrayList(@NonNull Class<?>[] classes) {
        ArrayList<Class<?>> result = new ArrayList<>();
        Collections.addAll(result, classes);
        return result;
    }

    @NonNull
    public static Factory getInstance() {
        return INSTANCE;
    }
}
