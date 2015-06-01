package ru.spbau.kozlov.cw02;

import checkers.nullness.quals.NonNull;
import checkers.nullness.quals.Nullable;

import java.util.ArrayList;

/**
 * @author adkozlov
 */
public class Builder {

    @Nullable
    public Object createInstance(@NonNull String canonicalName) throws ClassNotFoundException {
        ArrayList<ArrayList<Class<?>>> parameters = Factory.getInstance().getParameters(canonicalName);

        for (ArrayList<Class<?>> constructorParameters : parameters) {
            Object[] arguments = new Object[constructorParameters.size()];
            for (int i = 0; i < constructorParameters.size(); i++) {
                arguments[i] = createArgumentInstance(constructorParameters.get(i).getCanonicalName());
            }

            Object result = Factory.getInstance().createInstance(canonicalName, arguments);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    @NonNull
    private Object createArgumentInstance(@NonNull String canonicalName) throws ClassNotFoundException {
        try {
            return Class.forName(canonicalName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            // ignoring
        }
        return createInstance(canonicalName);
    }
}
