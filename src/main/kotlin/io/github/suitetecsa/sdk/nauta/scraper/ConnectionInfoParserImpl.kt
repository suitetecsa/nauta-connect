package io.github.suitetecsa.sdk.nauta.scraper

import io.github.suitetecsa.sdk.nauta.exception.InvalidSessionException
import io.github.suitetecsa.sdk.nauta.exception.LoadInfoException
import io.github.suitetecsa.sdk.nauta.exception.NautaException
import io.github.suitetecsa.sdk.nauta.model.AccountInfo
import io.github.suitetecsa.sdk.nauta.model.ConnectInformation
import io.github.suitetecsa.sdk.nauta.model.LastConnection
import io.github.suitetecsa.sdk.nauta.network.HttpResponse
import io.github.suitetecsa.sdk.nauta.utils.*
import io.github.suitetecsa.sdk.nauta.utils.Constants.CONNECT_DOMAIN
import org.jetbrains.annotations.Contract
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.regex.Pattern
import kotlin.math.min

/**
 * Implementación de `ConnectionInfoParser` para analizar información de conexión de Nauta.
 */
internal class ConnectionInfoParserImpl : ConnectionInfoParser {
    private val loadInfoExceptionHandler = ExceptionHandler { LoadInfoException(it) }

    /**
     * Analiza la tabla de información en el HTML y extrae los valores asociados a las claves dadas.
     *
     * @param htmlParsed El objeto `Document` parseado del contenido HTML.
     * @param keys Las claves que se utilizan para asociar los valores en la tabla.
     * @return Un mapa de pares clave-valor con la información extraída de la tabla.
     */
    private fun parseInfoTable(htmlParsed: Document, keys: List<String>): Map<String, String> {
        val info: MutableMap<String, String> = HashMap()
        val valueElements = htmlParsed.select("#sessioninfo > tbody > tr > :not(td.key)")

        val size = min(keys.size.toDouble(), valueElements.size.toDouble()).toInt()

        for (index in 0 until size) {
            val element = valueElements[index]
            info[keys[index]] = element.text().trim { it <= ' ' }
        }

        return info
    }

    /**
     * Analiza la tabla de últimas conexiones en el HTML y extrae los valores de las columnas especificadas.
     *
     * @param htmlParsed El objeto `Document` parseado del contenido HTML.
     * @param keys Las claves que se utilizan para asociar los valores en la tabla.
     * @return Una lista de objetos `LastConnection` con la información de las últimas conexiones.
     */
    private fun parseLastConnectionsTable(htmlParsed: Document, keys: List<String>): List<LastConnection> {
        val lastConnections: MutableList<LastConnection> = ArrayList()
        val connectionRows = htmlParsed.select("#sesiontraza > tbody > tr")

        for (row in connectionRows) {
            val connectionValues = row.select("td")
            val connectionMap: MutableMap<String, String> = HashMap()

            val NOT_LAST_CONNECTION_KEYS = 4
            for (index in NOT_LAST_CONNECTION_KEYS until keys.size) {
                val element = connectionValues[index - NOT_LAST_CONNECTION_KEYS]
                connectionMap[keys[index]] = element.text().trim { it <= ' ' }
            }

            lastConnections.add(
                LastConnection(
                    connectionMap.getOrDefault("from", ""),
                    connectionMap.getOrDefault("accountTime", ""),
                    connectionMap.getOrDefault("to", "")
                )
            )
        }

        return lastConnections
    }

    /**
     * Crea un objeto `AccountInfo` a partir del mapa de información proporcionado.
     *
     * @param info El mapa de información con claves y valores asociados.
     * @return Un objeto `AccountInfo` con la información de la cuenta.
     */
    @Contract("_ -> new")
    private fun createAccountInfo(info: Map<String, String>): AccountInfo {
        return AccountInfo(
            info.getOrDefault("access_areas", ""),
            info.getOrDefault("account_status", ""),
            info.getOrDefault("availableBalance", ""),
            info.getOrDefault("expiration_date", "")
        )
    }

    /**
     * Analiza el HTML para verificar si hay conexiones disponibles.
     *
     * @param response El contenido HTML a analizar.
     * @return `true` si hay conexiones disponibles, de lo contrario, `false`.
     */
    override fun parseCheckConnection(response: HttpResponse) = !response.text.contains(CONNECT_DOMAIN)

    /**
     * Analiza el HTML para extraer información de conexión de Nauta.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return Un objeto de tipo `NautaConnectInformation` que contiene la información de conexión.
     */
    @Throws(NautaException::class, LoadInfoException::class)
    override fun parseConnectInformation(httpResponse: HttpResponse): ConnectInformation {
        val keys: List<String> = mutableListOf(
            "account_status",
            "availableBalance",
            "expiration_date",
            "access_areas",
            "from",
            "to",
            "accountTime"
        )
        val htmlParsed = Jsoup.parse(httpResponse.text)

        DocumentUtils.throwExceptionOnFailure(
            htmlParsed,
            "Fail parse nauta account information",
            loadInfoExceptionHandler
        )

        val info = parseInfoTable(htmlParsed, keys)
        val lastConnections = parseLastConnectionsTable(htmlParsed, keys)

        return ConnectInformation(createAccountInfo(info), lastConnections)
    }

    /**
     * Analiza el HTML para extraer el tiempo restante de la conexión.
     *
     * @param html El contenido HTML a analizar.
     * @return El tiempo restante de la conexión en segundos.
     */
    @Throws(InvalidSessionException::class)
    override fun parseRemainingTime(html: String): Long {
        return StringUtils.toSeconds(html)
    }

    /**
     * Analiza el HTML para extraer el atributo UUID.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return El valor del atributo UUID.
     */
    @Throws(LoadInfoException::class)
    override fun parseAttributeUUID(httpResponse: HttpResponse): String {
        val matcher = Pattern.compile("ATTRIBUTE_UUID=(\\w+)&").matcher(httpResponse.text)

        if (matcher.find()) {
            return matcher.group(1)
        } else {
            var errorMessage = "Fail to parse attribute uuid"
            val ERROR_MESSAGE_LENGTH = 100
            if (httpResponse.text.length > ERROR_MESSAGE_LENGTH) {
                errorMessage += " - " + httpResponse.text.substring(0, ERROR_MESSAGE_LENGTH)
            }
            throw loadInfoExceptionHandler.handleException(errorMessage, listOf(httpResponse.text))
        }
    }


    override fun isSuccessLogout(httpResponse: HttpResponse): Boolean {
        return httpResponse.text.contains("SUCCESS")
    }
}
