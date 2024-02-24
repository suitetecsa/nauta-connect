package io.github.suitetecsa.sdk.nauta

import io.github.suitetecsa.sdk.nauta.ConnectionFactory.createConnection
import io.github.suitetecsa.sdk.nauta.exception.LoadInfoException
import io.github.suitetecsa.sdk.nauta.exception.NautaException
import io.github.suitetecsa.sdk.nauta.network.HttpResponse
import io.github.suitetecsa.sdk.nauta.network.ResponseUtils
import org.jetbrains.annotations.Contract
import org.jsoup.Connection
import org.jsoup.Connection.Response
import java.io.IOException

internal class SessionImpl : Session {
    private val cookies: MutableMap<String, String> = HashMap()

    /**
     * Crea y ejecuta una conexión con los parámetros proporcionados.
     *
     * @param url           URL de la solicitud.
     * @param requestData   Datos para la solicitud.
     * @param requestAction Función lambda que ejecuta la solicitud y devuelve la respuesta.
     * @return Objeto `ResultType<HttpResponse>` con los datos de la respuesta o información sobre el error, según
     * corresponda.
    </HttpResponse> */
    @Contract("_, _, _ -> new")
    @Throws(NautaException::class, LoadInfoException::class)
    private fun executeRequest(
        url: String,
        requestData: Map<String, String>?,
        requestAction: (Connection) -> Response
    ): HttpResponse {
        val connection =
            if (requestData == null) createConnection(url, cookies) else createConnection(url, requestData, cookies)
        val response = requestAction(connection)
        ResponseUtils.throwExceptionOnFailure(response, "There was a failure to communicate with the portal")

        cookies.putAll(response.cookies())

        return HttpResponse(
            response.statusCode(),
            response.statusMessage(),
            response.bodyAsBytes(),
            response.cookies()
        )
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
    </HttpResponse> */
    @Throws(NautaException::class, LoadInfoException::class)
    override fun get(
        url: String,
        params: Map<String, String>?,
        timeout: Int
    ): HttpResponse {
        return executeRequest(url, params) { connection: Connection ->
            try {
                return@executeRequest connection
                    .timeout(timeout)
                    .method(Connection.Method.GET)
                    .execute()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    @Throws(NautaException::class, LoadInfoException::class)
    override fun post(url: String, data: Map<String, String>?): HttpResponse {
        return executeRequest(url, data) { connection: Connection ->
            try {
                return@executeRequest connection
                    .method(Connection.Method.POST)
                    .execute()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }
}
