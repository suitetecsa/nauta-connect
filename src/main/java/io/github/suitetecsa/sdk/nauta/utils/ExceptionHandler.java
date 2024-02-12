package io.github.suitetecsa.sdk.nauta.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Clase que maneja excepciones y crea instancias de excepciones utilizando una factoría de excepciones.
 *
 */
public class ExceptionHandler<T extends Throwable> {
    private final ExceptionFactory<T> exceptionFactory;

    public ExceptionHandler(ExceptionFactory<T> exceptionFactory) {
        this.exceptionFactory = exceptionFactory;
    }

    /**
     * Maneja una excepción y crea una instancia de excepción utilizando la factoría de excepciones y los
     * mensajes de error dados.
     *
     * @param message El mensaje de la excepción.
     * @param errors Los mensajes de error adicionales (opcional).
     * @return Una instancia de excepción creada.
     */
    public T handleException(String message, @NotNull List<String> errors) {
        String errorMessage = errors.isEmpty() ? "No specific error message" : String.join("; ", errors);
        return exceptionFactory.createException(message + " :: " + errorMessage);
    }

    /**
     * Builder para construir una instancia de `ExceptionHandler`.
     *
     */
    public static class Builder<T extends Exception> {
        private Class<T> exceptionClass;
        private ExceptionFactory<T> excFactory = null;

        public Builder() {
        }

        /**
         * Establece la factoría de excepciones utilizada para crear las instancias de excepciones.
         *
         * @param exceptionFactory La factoría de excepciones a utilizar.
         * @return El builder actualizado.
         */
        public Builder<T> withExceptionFactory(ExceptionFactory<T> exceptionFactory) {
            this.excFactory = exceptionFactory;
            return this;
        }
        public Builder<T> withExceptionClass(Class<T> exceptionClass) {
            this.exceptionClass = exceptionClass;
            return this;
        }

        /**
         * Construye una instancia de `ExceptionHandler` utilizando la factoría de excepciones especificada.
         * Si no se proporciona una factoría de excepciones, se utiliza una instancia de `ExceptionFactoryImpl`
         * con la clase de excepción dada.
         *
         * @return La instancia de `ExceptionHandler` creada.
         */
        public ExceptionHandler<T> build() {
            if (excFactory == null) {
                excFactory = new ExceptionFactoryImpl.Builder<T>()
                        .withExceptionClass(exceptionClass)
                        .build();
            }
            return new ExceptionHandler<>(excFactory);
        }
    }
}
