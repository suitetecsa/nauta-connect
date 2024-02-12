package io.github.suitetecsa.sdk.nauta.scraper;

import io.github.suitetecsa.sdk.nauta.network.HttpResponse;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

/**
 * Interfaz que define un parser para analizar formularios en contenido HTML.
 */
public interface FormParser {
    /**
     * Analiza el HTML para extraer información y datos del formulario de acción.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return Un par que contiene la URL del formulario y un mapa de datos.
     */
    SimpleEntry<String, Map<String, String>> parseActionForm(HttpResponse httpResponse);

    /**
     * Analiza el HTML para extraer información y datos del formulario de inicio de sesión.
     *
     * @param httpResponse@return Un par que contiene la URL del formulario y un mapa de datos.
     */
    SimpleEntry<String, Map<String, String>> parseLoginForm(HttpResponse httpResponse);

    class Builder {
        public FormParser build() {
            return new FormParserImpl();
        }
    }
}
