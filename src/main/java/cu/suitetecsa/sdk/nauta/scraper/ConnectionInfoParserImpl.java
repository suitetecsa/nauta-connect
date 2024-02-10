package cu.suitetecsa.sdk.nauta.jsoupimpl;

import cu.suitetecsa.sdk.nauta.exception.InvalidSessionException;
import cu.suitetecsa.sdk.nauta.exception.LoadInfoException;
import cu.suitetecsa.sdk.nauta.exception.NautaException;
import cu.suitetecsa.sdk.nauta.model.AccountInfo;
import cu.suitetecsa.sdk.nauta.model.ConnectInformation;
import cu.suitetecsa.sdk.nauta.model.LastConnection;
import cu.suitetecsa.sdk.nauta.network.HttpResponse;
import cu.suitetecsa.sdk.nauta.scraper.ConnectionInfoParser;
import cu.suitetecsa.sdk.nauta.utils.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementación de `ConnectionInfoParser` para analizar información de conexión de Nauta.
 */
public class JsoupConnectionInfoParser implements ConnectionInfoParser {
    private final ExceptionHandler<LoadInfoException> loadInfoExceptionHandler = new ExceptionHandler<>(LoadInfoException::new);

    static String connectDomain = "secure.etecsa.net";

    /**
     * Analiza la tabla de información en el HTML y extrae los valores asociados a las claves dadas.
     *
     * @param htmlParsed El objeto `Document` parseado del contenido HTML.
     * @param keys Las claves que se utilizan para asociar los valores en la tabla.
     * @return Un mapa de pares clave-valor con la información extraída de la tabla.
     */
    private @NotNull Map<String, String> parseInfoTable(@NotNull Document htmlParsed, @NotNull List<String> keys) {
        Map<String, String> info = new HashMap<>();
        Elements valueElements = htmlParsed.select("#sessioninfo > tbody > tr > :not(td.key)");

        int size = Math.min(keys.size(), valueElements.size());

        for (int index = 0; index < size; index++) {
            Element element = valueElements.get(index);
            info.put(keys.get(index), element.text().trim());
        }

        return info;
    }

    /**
     * Analiza la tabla de últimas conexiones en el HTML y extrae los valores de las columnas especificadas.
     *
     * @param htmlParsed El objeto `Document` parseado del contenido HTML.
     * @param keys Las claves que se utilizan para asociar los valores en la tabla.
     * @return Una lista de objetos `LastConnection` con la información de las últimas conexiones.
     */
    private @NotNull List<LastConnection> parseLastConnectionsTable(@NotNull Document htmlParsed, List<String> keys) {
        List<LastConnection> lastConnections = new ArrayList<>();
        Elements connectionRows = htmlParsed.select("#sesiontraza > tbody > tr");

        for (Element row : connectionRows) {
            Elements connectionValues = row.select("td");
            Map<String, String> connectionMap = new HashMap<>();

            int NOT_LAST_CONNECTION_KEYS = 4;
            for (int index = NOT_LAST_CONNECTION_KEYS; index < keys.size(); index++) {
                Element element = connectionValues.get(index - NOT_LAST_CONNECTION_KEYS);
                connectionMap.put(keys.get(index), element.text().trim());
            }

            lastConnections.add(new LastConnection(
                    connectionMap.getOrDefault("from", ""),
                    connectionMap.getOrDefault("accountTime", ""),
                    connectionMap.getOrDefault("to", "")
            ));
        }

        return lastConnections;
    }

    /**
     * Crea un objeto `AccountInfo` a partir del mapa de información proporcionado.
     *
     * @param info El mapa de información con claves y valores asociados.
     * @return Un objeto `AccountInfo` con la información de la cuenta.
     */
    @Contract("_ -> new")
    private @NotNull AccountInfo createAccountInfo(@NotNull Map<String, String> info) {
        return new AccountInfo(
                info.getOrDefault("access_areas", ""),
                info.getOrDefault("account_status", ""),
                info.getOrDefault("availableBalance", ""),
                info.getOrDefault("expiration_date", "")
        );
    }

    /**
     * Analiza el HTML para verificar si hay conexiones disponibles.
     *
     * @param html El contenido HTML a analizar.
     * @return `true` si hay conexiones disponibles, de lo contrario, `false`.
     */
    @Override
    public Boolean parseCheckConnection(@NotNull String html) {
        return !html.contains(connectDomain);
    }

    /**
     * Analiza el HTML para extraer información de conexión de Nauta.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return Un objeto de tipo `NautaConnectInformation` que contiene la información de conexión.
     */
    @Override
    public ConnectInformation parseConnectInformation(@NotNull HttpResponse httpResponse) throws NautaException, LoadInfoException {
        List<String> keys = Arrays.asList("account_status", "availableBalance", "expiration_date", "access_areas", "from", "to", "accountTime");
        Document htmlParsed = Jsoup.parse(httpResponse.getText());

        DocumentUtils.throwExceptionOnFailure(
                htmlParsed,
                "Fail parse nauta account information",
                PortalManager.CONNECT,
                loadInfoExceptionHandler
        );

        Map<String, String> info = parseInfoTable(htmlParsed, keys);
        List<LastConnection> lastConnections = parseLastConnectionsTable(htmlParsed, keys);

        return new ConnectInformation(createAccountInfo(info), lastConnections);
    }

    /**
     * Analiza el HTML para extraer el tiempo restante de la conexión.
     *
     * @param html El contenido HTML a analizar.
     * @return El tiempo restante de la conexión en segundos.
     */
    @Override
    public Long parseRemainingTime(String html) throws InvalidSessionException {
        return StringUtils.toSeconds(html);
    }

    /**
     * Analiza el HTML para extraer el atributo UUID.
     *
     * @param httpResponse El contenido HTML a analizar.
     * @return El valor del atributo UUID.
     */
    @Override
    public String parseAttributeUUID(@NotNull HttpResponse httpResponse) throws LoadInfoException {

        Matcher matcher = Pattern.compile("ATTRIBUTE_UUID=(\\w+)&").matcher(httpResponse.getText());

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            String errorMessage = "Fail to parse attribute uuid";
            int ERROR_MESSAGE_LENGTH = 100;
            if (httpResponse.getText().length() > ERROR_MESSAGE_LENGTH) {
                errorMessage += " - " + httpResponse.getText().substring(0, ERROR_MESSAGE_LENGTH);
            }
            throw loadInfoExceptionHandler.handleException(errorMessage, Collections.singletonList(httpResponse.getText()));
        }
    }


    @Override
    public Boolean isSuccessLogout(@NotNull HttpResponse httpResponse) {
        return httpResponse.getText().contains("SUCCESS");
    }
}
