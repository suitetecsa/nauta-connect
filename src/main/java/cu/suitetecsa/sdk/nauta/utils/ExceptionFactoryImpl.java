package cu.suitetecsa.sdk.nauta.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

public class ExceptionFactoryImpl<T extends Throwable> implements ExceptionFactory<T> {

    @SuppressWarnings("unchecked")
    private Class<T> inferExceptionClass() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    /**
     * Crea una instancia de excepción utilizando la clase de excepción y el mensaje dado.
     *
     * @param message El mensaje de la excepción.
     * @return Una instancia de excepción creada.
     */
    @SuppressWarnings("unchecked")
    @Override
    public T createException(String message) {
        Class<T> exceptionClass = inferExceptionClass();
        try {
            Constructor<T> constructor = exceptionClass.getDeclaredConstructor(String.class);
            return constructor.newInstance(message);
        } catch (Exception e) {
            return (T) new Exception("Error al crear la excepción", e);
        }
    }

    public static class Builder<T extends Exception> {
        public ExceptionFactoryImpl<T> build() {
            return new ExceptionFactoryImpl<>();
        }
    }
}
