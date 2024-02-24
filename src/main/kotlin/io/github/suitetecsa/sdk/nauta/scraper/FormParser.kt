package io.github.suitetecsa.sdk.nauta.scraper

import io.github.suitetecsa.sdk.nauta.network.HttpResponse
import java.util.*

/**
 * Interfaz que define un parser para analizar formularios en contenido HTML.
 */
interface FormParser {
    /**
     * Analiza el HTML para extraer información y datos del formulario de acción.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return Un par que contiene la URL del formulario y un mapa de datos.
     */
    fun parseActionForm(httpResponse: HttpResponse): AbstractMap.SimpleEntry<String, Map<String, String>>

    /**
     * Analiza el HTML para extraer información y datos del formulario de inicio de sesión.
     *
     * @param httpResponse@return Un par que contiene la URL del formulario y un mapa de datos.
     */
    fun parseLoginForm(httpResponse: HttpResponse): AbstractMap.SimpleEntry<String, Map<String, String>>

    class Builder {
        fun build(): FormParser {
            return FormParserImpl()
        }
    }
}
