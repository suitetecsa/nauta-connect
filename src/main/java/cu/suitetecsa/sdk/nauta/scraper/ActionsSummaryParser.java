package cu.suitetecsa.sdk.nauta.scraper;

import cu.suitetecsa.sdk.nauta.exception.InvalidSessionException;
import cu.suitetecsa.sdk.nauta.exception.LoadInfoException;
import cu.suitetecsa.sdk.nauta.exception.NotLoggedInException;
import cu.suitetecsa.sdk.nauta.model.ConnectionsSummary;
import cu.suitetecsa.sdk.nauta.model.FeesPaidSummary;
import cu.suitetecsa.sdk.nauta.model.RechargesSummary;
import cu.suitetecsa.sdk.nauta.model.TransfersSummary;
import cu.suitetecsa.sdk.nauta.network.HttpResponse;

public interface ActionsSummaryParser {
    /**
     * Analiza el HTML para extraer un resumen de conexiones.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return El objeto `ConnectionsSummary` que contiene el resumen de conexiones.
     */
    ConnectionsSummary parseConnectionsSummary(HttpResponse httpResponse) throws LoadInfoException, NotLoggedInException, InvalidSessionException;

    /**
     * Analiza el HTML para extraer un resumen de recargas.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return El objeto `RechargesSummary` que contiene el resumen de recargas.
     */
    RechargesSummary parseRechargesSummary(HttpResponse httpResponse) throws LoadInfoException, NotLoggedInException;

    /**
     * Analiza el HTML para extraer un resumen de transferencias.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return El objeto `TransfersSummary` que contiene el resumen de transferencias.
     */
    TransfersSummary parseTransfersSummary(HttpResponse httpResponse) throws LoadInfoException, NotLoggedInException;

    /**
     * Analiza el HTML para extraer un resumen de cotizaciones pagadas.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return El objeto `QuotesPaidSummary` que contiene el resumen de cotizaciones pagadas.
     */
    FeesPaidSummary parseFeesPaidSummary(HttpResponse httpResponse) throws LoadInfoException, NotLoggedInException;

    class Builder {
        public ActionsSummaryParser build() {
            return new ActionsSummaryParserImpl();
        }
    }
}
