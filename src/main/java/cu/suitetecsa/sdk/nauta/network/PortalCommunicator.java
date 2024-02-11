package cu.suitetecsa.sdk.nauta.network;

import cu.suitetecsa.sdk.nauta.Session;
import cu.suitetecsa.sdk.nauta.exception.LoadInfoException;
import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException;
import cu.suitetecsa.sdk.nauta.exception.NautaException;

import java.util.function.Function;

/**
 * Esta interfaz define un comunicador para interactuar con el portal de conexión.
 * Permite realizar acciones y transformar las respuestas utilizando un transformador personalizado.
 */
public interface PortalCommunicator {
    /**
     * Realiza una acción en el portal de conexión y transforma la respuesta según la función dada.
     *
     * @param action La acción que se va a realizar en el portal.
     * @param transform La función de transformación que se aplicará a la respuesta del portal.
     * @param <T> El tipo de objeto que se espera como resultado.
     * @return Objeto `ResultType` que encapsula el resultado de la acción realizada y transformada.
     */
    <T> T performRequest(Action action, Function<HttpResponse, T> transform) throws NautaAttributeException, NautaException, LoadInfoException;

    /**
     * Realiza una acción en el portal de conexión y transforma la respuesta según la función dada.
     *
     * @param url La URL a la que se va a realizar la solicitud.
     * @param transform La función de transformación que se aplicará a la respuesta del portal.
     * @param <T> El tipo de objeto que se espera como resultado.
     * @return Objeto `ResultType` que encapsula el resultado de la acción realizada y transformada.
     */
    <T> T performRequest(String url, Function<HttpResponse, T> transform) throws NautaException, LoadInfoException;

    class Builder {
        private Session session;

        public Builder withSession(Session session) {
            this.session = session;
            return this;
        }

        public PortalCommunicator build() {
            if (session == null) session = new Session.Builder().build();
            return new PortalCommunicatorImpl(session);
        }
    }
}
