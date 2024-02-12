package io.github.suitetecsa.sdk.nauta.utils;

import io.github.suitetecsa.sdk.nauta.exception.NautaException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ExceptionFactoryImpl<T extends Throwable> implements ExceptionFactory<T> {
    private final Class<T> exceptionClass;

    public ExceptionFactoryImpl(Class<T> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }


    /**
     * Crea una instancia de excepción utilizando la clase de excepción y el mensaje dado.
     *
     * @param message El mensaje de la excepción.
     * @return Una instancia de excepción creada.
     */
    @Override
    public T createException(String message) {
        Constructor<T> constructor;
        try {
            constructor = exceptionClass.getDeclaredConstructor(String.class);
            return constructor.newInstance(message);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Builder<T extends Exception> {
        private Class<T> exceptionClass;

        public Builder<T> withExceptionClass(Class<T> exceptionClass) {
            this.exceptionClass = exceptionClass;
            return this;
        }

        @SuppressWarnings("unchecked")
        public ExceptionFactoryImpl<T> build() {
            if (exceptionClass == null) exceptionClass = (Class<T>) NautaException.class;
            return new ExceptionFactoryImpl<>(exceptionClass);
        }
    }
}
