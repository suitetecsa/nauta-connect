package cu.suitetecsa.sdk.nauta.scraper;

import cu.suitetecsa.sdk.nauta.exception.InvalidSessionException;
import cu.suitetecsa.sdk.nauta.exception.LoadInfoException;
import cu.suitetecsa.sdk.nauta.exception.NautaException;
import cu.suitetecsa.sdk.nauta.model.Connection;
import cu.suitetecsa.sdk.nauta.model.FeePaid;
import cu.suitetecsa.sdk.nauta.model.Recharge;
import cu.suitetecsa.sdk.nauta.model.Transfer;
import cu.suitetecsa.sdk.nauta.network.HttpResponse;
import cu.suitetecsa.sdk.nauta.utils.DocumentUtils;
import cu.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;
import cu.suitetecsa.sdk.nauta.utils.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class ActionsParserImpl implements ActionsParser {
    private final ExceptionHandler<LoadInfoException> loadInfoExceptionHandler = new ExceptionHandler<>(LoadInfoException::new);

    @Contract("_ -> new")
    private @NotNull Connection parseConnection(@NotNull Element element) throws ParseException, InvalidSessionException {
        return new Connection(
                StringUtils.toDateMillis(element.select("td").get(0).text()),
                StringUtils.toDateMillis(element.select("td").get(1).text()),
                StringUtils.toSeconds(element.select("td").get(2).text()),
                StringUtils.toBytes(element.select("td").get(3).text()),
                StringUtils.toBytes(element.select("td").get(4).text()),
                StringUtils.fromPriceString(element.select("td").get(5).text())
        );
    }

    @Contract("_ -> new")
    private @NotNull Recharge parseRecharge(@NotNull Element element) throws ParseException {
        return new Recharge(
                StringUtils.toDateMillis(element.select("td").get(0).text()),
                StringUtils.fromPriceString(element.select("td").get(1).text()),
                element.select("td").get(2).text(),
                element.select("td").get(3).text()
        );
    }

    @Contract("_ -> new")
    private @NotNull Transfer parseTransfer(@NotNull Element element) throws ParseException {
        return new Transfer(
                StringUtils.toDateMillis(element.select("td").get(0).text()),
                StringUtils.fromPriceString(element.select("td").get(2).text()),
                element.select("td").get(2).text()
        );
    }

    @Contract("_ -> new")
    private @NotNull FeePaid parseFeePaid(@NotNull Element element) throws ParseException {
        return new FeePaid(
                StringUtils.toDateMillis(element.select("td").get(0).text()),
                StringUtils.fromPriceString(element.select("td").get(1).text()),
                element.select("td").get(2).text(),
                element.select("td").get(3).text(),
                element.select("td").get(4).text()
        );
    }

    private <T> List<T> parseList(@NotNull HttpResponse httpResponse, Function<Element, T> constructor) throws NautaException, LoadInfoException {
        Document parsedHtml = Jsoup.parse(httpResponse.getText());
        DocumentUtils.throwExceptionOnFailure(parsedHtml, "Fail to get action list", PortalManager.USER, loadInfoExceptionHandler);
        Element tableBody = parsedHtml.selectFirst(".responsive-table > tbody");
        if (tableBody != null) {
            return tableBody.select("tr").stream().map(constructor).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Connection> parseConnections(HttpResponse httpResponse) throws NautaException, LoadInfoException {
        return parseList(httpResponse, element -> {
            try {
                return parseConnection(element);
            } catch (ParseException | InvalidSessionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<Recharge> parseRecharges(HttpResponse httpResponse) throws NautaException, LoadInfoException {
        return parseList(httpResponse, element -> {
            try {
                return parseRecharge(element);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<Transfer> parseTransfers(HttpResponse httpResponse) throws NautaException, LoadInfoException {
        return parseList(httpResponse, element -> {
            try {
                return parseTransfer(element);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<FeePaid> parseFeesPaid(HttpResponse httpResponse) throws NautaException, LoadInfoException {
        return parseList(httpResponse, element -> {
            try {
                return parseFeePaid(element);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
