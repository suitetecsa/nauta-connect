package cu.suitetecsa.sdk.nauta.scraper;

import cu.suitetecsa.sdk.nauta.exception.LoadInfoException;
import cu.suitetecsa.sdk.nauta.exception.NautaException;
import cu.suitetecsa.sdk.nauta.model.Connection;
import cu.suitetecsa.sdk.nauta.model.FeePaid;
import cu.suitetecsa.sdk.nauta.model.Recharge;
import cu.suitetecsa.sdk.nauta.model.Transfer;
import cu.suitetecsa.sdk.nauta.network.HttpResponse;

import java.util.List;

public interface ActionsParser {
    /**
     * Analiza el HTML para extraer la lista de conexiones.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return La lista de objetos `Connection` que contienen la información de las conexiones.
     */
    List<Connection> parseConnections(HttpResponse httpResponse) throws NautaException, LoadInfoException;

    /**
     * Analiza el HTML para extraer la lista de recargas.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return La lista de objetos `Recharge` que contienen la información de las recargas.
     */
    List<Recharge> parseRecharges(HttpResponse httpResponse) throws NautaException, LoadInfoException;

    /**
     * Analiza el HTML para extraer la lista de transferencias.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return La lista de objetos `Transfer` que contienen la información de las transferencias.
     */
    List<Transfer> parseTransfers(HttpResponse httpResponse) throws NautaException, LoadInfoException;

    /**
     * Analiza el HTML para extraer la lista de cotizaciones pagadas.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return La lista de objetos `QuotePaid` que contienen la información de las cotizaciones pagadas.
     */
    List<FeePaid> parseFeesPaid(HttpResponse httpResponse) throws NautaException, LoadInfoException;

    class Builder {
        public ActionsParser build() {
            return new ActionsParserImpl();
        }
    }
}
