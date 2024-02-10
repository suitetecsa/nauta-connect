package cu.suitetecsa.sdk.nauta.network;

import cu.suitetecsa.sdk.nauta.exception.NautaException;
import cu.suitetecsa.sdk.nauta.utils.ExceptionFactory;
import cu.suitetecsa.sdk.nauta.utils.ExceptionFactoryImpl;
import cu.suitetecsa.sdk.nauta.utils.ExceptionHandler;
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
        throwExceptionOnFailure(response, message, new ExceptionFactoryImpl.Builder<NautaException>().build());
    }

    /**
     * Esta función es un método estático llamado throwExceptionOnFailure que es una extensión de la clase Response de Connection.
     * Lanza una excepción si la respuesta indica un fallo basado en el código de estado.
     *
     * @param message           El mensaje de error a incluir en la excepción.
     * @param exceptionFactory  La factoría para crear la excepción (opcional).
     */
    public static <T extends Exception> void throwExceptionOnFailure(Response response, String message, ExceptionFactory<T> exceptionFactory) throws T {
        int statusCode = response.statusCode();
        int STATUS_REDIRECT_RANGE_END = 399;
        int STATUS_REDIRECT_RANGE_START = 300;
        int STATUS_OK_RANGE_END = 299;
        int STATUS_OK_RANGE_START = 200;
        if (!(statusCode >= STATUS_OK_RANGE_START && statusCode <= STATUS_OK_RANGE_END) &&
                !(statusCode >= STATUS_REDIRECT_RANGE_START && statusCode <= STATUS_REDIRECT_RANGE_END && response.hasHeader("Location"))) {
            throw new ExceptionHandler.Builder<T>().withExceptionFactory(exceptionFactory).build()
                    .handleException(message, List.of(response.statusMessage()));
        }
    }
}
