package io.github.suitetecsa.sdk.nauta;

import io.github.suitetecsa.sdk.nauta.exception.LoadInfoException;
import io.github.suitetecsa.sdk.nauta.exception.NautaException;
import io.github.suitetecsa.sdk.nauta.network.HttpResponse;

import java.util.Map;

/**
 * Esta clase representa una sesión de comunicación con el portal Nauta (Portal Cautivo, Portal de Usuario).
 * Mantiene las cookies para permitir una comunicación continua.
 */
public interface Session {
    /**
     * Realiza una solicitud GET al portal Nauta.
     *
     * @param url URL a la que se realiza la solicitud.
     * @return Objeto `HttpResponse` con los datos de la respuesta o información sobre el error,
     * según corresponda.
     */
    HttpResponse get(String url) throws NautaException, LoadInfoException;

    /**
     * Realiza una solicitud GET al portal Nauta.
     *
     * @param url URL a la que se realiza la solicitud.
     * @param params Parámetros de la solicitud (opcional).
     * @return Objeto `HttpResponse` con los datos de la respuesta o información sobre el error,
     * según corresponda.
     */
    HttpResponse get(String url, Map<String, String> params) throws NautaException, LoadInfoException;

    /**
     * Realiza una solicitud GET al portal Nauta.
     *
     * @param url URL a la que se realiza la solicitud.
     * @param params Parámetros de la solicitud (opcional).
     * @param ignoreContentType Ignorar el tipo de contenido devuelto en la respuesta (por defecto: `false`).
     * @return Objeto `HttpResponse` con los datos de la respuesta o información sobre el error,
     * según corresponda.
     */
    HttpResponse get(String url, Map<String, String> params, Boolean ignoreContentType) throws NautaException, LoadInfoException;

    /**
     * Realiza una solicitud GET al portal Nauta.
     *
     * @param url URL a la que se realiza la solicitud.
     * @param params Parámetros de la solicitud (opcional).
     * @param ignoreContentType Ignorar el tipo de contenido devuelto en la respuesta (por defecto: `false`).
     * @param timeout Tiempo límite para la solicitud (por defecto: `30000` milisegundos).
     * @return Objeto `HttpResponse` con los datos de la respuesta o información sobre el error,
     * según corresponda.
     */
    HttpResponse get(String url, Map<String, String> params, Boolean ignoreContentType, Integer timeout) throws NautaException, LoadInfoException;

    /**
     * Realiza una solicitud POST al portal Nauta.
     *
     * @param url URL a la que se realiza la solicitud.
     * @return Objeto `HttpResponse` con los datos de la respuesta o información sobre el error,
     * según corresponda.
     */
    HttpResponse post(String url) throws NautaException, LoadInfoException;

    /**
     * Realiza una solicitud POST al portal Nauta.
     *
     * @param url URL a la que se realiza la solicitud.
     * @param data Datos de la solicitud (opcional).
     * @return Objeto `HttpResponse` con los datos de la respuesta o información sobre el error,
     * según corresponda.
     */
    HttpResponse post(String url, Map<String, String> data) throws NautaException, LoadInfoException;

    class Builder {
        public Session build() {
            return new SessionImpl();
        }
    }
}
