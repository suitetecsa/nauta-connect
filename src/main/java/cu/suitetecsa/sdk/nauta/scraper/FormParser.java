package cu.suitetecsa.sdk.nauta.scraper;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

/**
 * Interfaz que define un parser para analizar formularios en contenido HTML.
 */
public interface FormParser {
    /**
     * Analiza el HTML para extraer información y datos del formulario de acción.
     *
     * @param html El contenido HTML a analizar.
     * @return Un par que contiene la URL del formulario y un mapa de datos.
     */
    SimpleEntry<String, Map<String, String>> parseActionForm(String html);

    /**
     * Analiza el HTML para extraer información y datos del formulario de inicio de sesión.
     *
     * @param html El contenido HTML a analizar.
     * @return Un par que contiene la URL del formulario y un mapa de datos.
     */
    SimpleEntry<String, Map<String, String>> parseLoginForm(String html);
}
