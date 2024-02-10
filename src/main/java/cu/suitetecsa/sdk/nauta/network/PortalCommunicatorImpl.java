package cu.suitetecsa.sdk.nauta.jsoupimpl;

import cu.suitetecsa.sdk.nauta.exception.LoadInfoException;
import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException;
import cu.suitetecsa.sdk.nauta.exception.NautaException;
import cu.suitetecsa.sdk.nauta.network.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

/**
 * Clase que implementa el comunicador del portal utilizando Jsoup.
 */
public class JsoupPortalCommunicator implements PortalCommunicator {
    private final Session session;

    public JsoupPortalCommunicator(Session session) {
        this.session = session;
    }

    /**
     * Maneja la respuesta de una solicitud al portal y la transforma según una función dada.
     *
     * @param url               La URL a la que se realiza la solicitud.
     * @param data              Datos para la solicitud (opcional).
     * @param method            El método HTTP utilizado para la solicitud (por defecto es GET).
     * @param ignoreContentType Indica si se debe ignorar el tipo de contenido en la respuesta (por defecto es `false`).
     * @param timeout           El tiempo límite para la solicitud en milisegundos (por defecto es 30000).
     * @param transform         La función de transformación que se aplicará a la respuesta del portal.
     * @return Resultado encapsulado en un objeto `ResultType` que contiene el resultado transformado o un error.
     */
    public <T> T handleResponse(
            String url,
            Map<String, String> data,
            @NotNull HttpMethod method,
            boolean ignoreContentType,
            int timeout,
            Function<HttpResponse, T> transform
    ) throws NautaException, LoadInfoException {
        HttpResponse response = switch (method) {
            case POST -> session.post(url, data);
            case GET -> session.get(url, data, ignoreContentType, timeout);
        };
        return transform.apply(response);
    }

    /**
     * Realiza una acción en el portal de conexión y transforma la respuesta utilizando la sesión dada.
     *
     * @param action    La acción que se va a realizar en el portal.
     * @param transform La función de transformación que se aplicará a la respuesta del portal.
     * @return Objeto `ResultType` que encapsula el resultado de la acción realizada y transformada.
     */
    @Override
    public <T> T performRequest(@NotNull Action action, Function<HttpResponse, T> transform) throws NautaAttributeException, NautaException, LoadInfoException {
        return handleResponse(action.getUrl(), action.getData(), action.getMethod(), action.isIgnoreContentType(), action.getTimeout(), transform);
    }

    /**
     * Realiza una acción en el portal de conexión y transforma la respuesta utilizando la URL dada.
     *
     * @param url       La URL a la que se realiza la solicitud.
     * @param transform La función de transformación que se aplicará a la respuesta del portal.
     * @return Objeto `ResultType` que encapsula el resultado de la acción realizada y transformada.
     */
    @Override
    public <T> T performRequest(String url, Function<HttpResponse, T> transform) throws NautaException, LoadInfoException {
        return handleResponse(url, null, HttpMethod.GET, false, 30000, transform);
    }
}
