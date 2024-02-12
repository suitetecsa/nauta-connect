package io.github.suitetecsa.sdk.nauta.network;

import io.github.suitetecsa.sdk.nauta.exception.NautaException;
import io.github.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection.Response;

import java.util.List;

public class ResponseUtils {
    /**
     * Esta función es un método estático llamado throwExceptionOnFailure que es una extensión de la clase Response de Connection.
     * Lanza una excepción si la respuesta indica un fallo basado en el código de estado.
     *
     * @param message           El mensaje de error a incluir en la excepción.
     */
    public static void throwExceptionOnFailure(Response response, String message) throws NautaException {
        throwExceptionOnFailure(response, message, new ExceptionHandler<>(NautaException::new));
    }

    /**
     * Esta función es un método estático llamado throwExceptionOnFailure que es una extensión de la clase Response de Connection.
     * Lanza una excepción si la respuesta indica un fallo basado en el código de estado.
     *
     * @param message           El mensaje de error a incluir en la excepción.
     * @param exceptionHandler  La factoría para crear la excepción (opcional).
     */
    public static <T extends Exception> void throwExceptionOnFailure(@NotNull Response response, String message, ExceptionHandler<T> exceptionHandler) throws T {
        int statusCode = response.statusCode();
        int STATUS_REDIRECT_RANGE_END = 399;
        int STATUS_REDIRECT_RANGE_START = 300;
        int STATUS_OK_RANGE_END = 299;
        int STATUS_OK_RANGE_START = 200;
        if (!(statusCode >= STATUS_OK_RANGE_START && statusCode <= STATUS_OK_RANGE_END) &&
                !(statusCode >= STATUS_REDIRECT_RANGE_START && statusCode <= STATUS_REDIRECT_RANGE_END && response.hasHeader("Location"))) {
            throw exceptionHandler
                    .handleException(message, List.of(response.statusMessage()));
        }
    }
}
