package io.github.suitetecsa.sdk.nauta.scraper;

import io.github.suitetecsa.sdk.nauta.exception.InvalidSessionException;
import io.github.suitetecsa.sdk.nauta.exception.LoadInfoException;
import io.github.suitetecsa.sdk.nauta.exception.NautaException;
import io.github.suitetecsa.sdk.nauta.model.ConnectInformation;
import io.github.suitetecsa.sdk.nauta.network.HttpResponse;

/**
 * Interfaz que define un scraper para analizar contenido HTML en el portal de conexión.
 * Proporciona métodos para extraer información específica y transformarla en objetos y resultados.
 */
public interface ConnectionInfoParser {

    /**
     * Analiza el HTML para verificar si hay conexiones disponibles.
     *
     * @param html El contenido HTML a analizar.
     * @return `true` si hay conexiones disponibles, de lo contrario, `false`.
     */
    Boolean parseCheckConnection(String html);

    /**
     * Analiza el HTML para extraer información de conexión de Nauta.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return Un objeto de tipo `ConnectInformation` que contiene la información de conexión.
     */
    ConnectInformation parseConnectInformation(HttpResponse httpResponse) throws NautaException, LoadInfoException;

    /**
     * Analiza el HTML para extraer el tiempo restante de la conexión.
     *
     * @param html El contenido HTML a analizar.
     * @return El tiempo restante de la conexión en segundos.
     */
    Long parseRemainingTime(String html) throws InvalidSessionException;

    /**
     * Analiza el HTML para extraer el atributo UUID.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return El valor del atributo UUID.
     */
    String parseAttributeUUID(HttpResponse httpResponse) throws LoadInfoException;

    /**
     * Analiza el HTML para verificar si el cierre de sesión fue exitoso.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return `true` si el cierre de sesión fue exitoso, de lo contrario, `false`.
     */
    Boolean isSuccessLogout(HttpResponse httpResponse);

    class Builder {
        public ConnectionInfoParser build() {
            return new ConnectionInfoParserImpl();
        }
    }
}
