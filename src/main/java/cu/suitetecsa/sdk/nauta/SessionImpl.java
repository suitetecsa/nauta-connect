package cu.suitetecsa.sdk.nauta;

import cu.suitetecsa.sdk.nauta.exception.LoadInfoException;
import cu.suitetecsa.sdk.nauta.exception.NautaException;
import cu.suitetecsa.sdk.nauta.network.HttpResponse;
import cu.suitetecsa.sdk.nauta.network.ResponseUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class SessionImpl implements Session {
    public final Map<String, String> cookies = new HashMap<>();

    /**
     * Crea y ejecuta una conexión con los parámetros proporcionados.
     *
     * @param url           URL de la solicitud.
     * @param requestData   Datos para la solicitud.
     * @param requestAction Función lambda que ejecuta la solicitud y devuelve la respuesta.
     * @return Objeto `ResultType<HttpResponse>` con los datos de la respuesta o información sobre el error, según
     * corresponda.
     */
    @Contract("_, _, _ -> new")
    private @NotNull HttpResponse executeRequest(
            String url,
            Map<String, String> requestData,
            @NotNull RequestAction requestAction
    ) throws NautaException, LoadInfoException {
        Connection connection = requestData == null ? ConnectionFactory.createConnection(url, cookies) : ConnectionFactory.createConnection(url, requestData, cookies);
        Connection.Response response = requestAction.apply(connection);
        ResponseUtils.throwExceptionOnFailure(response, "There was a failure to communicate with the portal");

        cookies.putAll(response.cookies());

        return new HttpResponse(
                response.statusCode(),
                response.statusMessage(),
                response.bodyAsBytes(),
                response.cookies()
        );
    }

    // Definición de la interfaz funcional para requestAction
    @FunctionalInterface
    private interface RequestAction {
        Connection.Response apply(Connection connection) throws LoadInfoException;
    }

    @Override
    public HttpResponse get(String url) throws NautaException, LoadInfoException {
        return executeRequest(url, null, connection -> {
            try {
                return connection
                        .ignoreContentType(false)
                        .timeout(30000)
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public HttpResponse get(String url, Map<String, String> params) throws NautaException, LoadInfoException {
        return executeRequest(url, params, connection -> {
            try {
                return connection
                        .ignoreContentType(false)
                        .timeout(30000)
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public HttpResponse get(String url, Map<String, String> params, Boolean ignoreContentType) throws NautaException, LoadInfoException {
        return executeRequest(url, params, connection -> {
            try {
                return connection
                        .ignoreContentType(ignoreContentType)
                        .timeout(30000)
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Realiza una solicitud GET al portal Nauta.
     *
     * @param url               URL a la que se realiza la solicitud.
     * @param params            Parámetros de la solicitud (opcional).
     * @param ignoreContentType Ignorar el tipo de contenido devuelto en la respuesta (por defecto: `false`).
     * @param timeout           Tiempo límite para la solicitud (por defecto: `30000` milisegundos).
     * @return Objeto `ResultType<HttpResponse>` con los datos de la respuesta o información sobre el error, según
     * corresponda.
     */
    @Override
    public HttpResponse get(String url, Map<String, String> params, Boolean ignoreContentType, Integer timeout) throws NautaException, LoadInfoException {
        return executeRequest(url, params, connection -> {
            try {
                return connection
                        .ignoreContentType(ignoreContentType)
                        .timeout(timeout)
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public HttpResponse post(String url) throws NautaException, LoadInfoException {
        return executeRequest(url, null, connection -> {
            try {
                return connection
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public HttpResponse post(String url, Map<String, String> data) throws NautaException, LoadInfoException {
        return executeRequest(url, data, connection -> {
            try {
                return connection
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
