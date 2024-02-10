package cu.suitetecsa.sdk.nauta.jsoupimpl;

import cu.suitetecsa.sdk.nauta.exception.InvalidSessionException;
import cu.suitetecsa.sdk.nauta.exception.NautaGetInfoException;
import cu.suitetecsa.sdk.nauta.exception.NotLoggedInException;
import cu.suitetecsa.sdk.nauta.model.AccountDetail;
import cu.suitetecsa.sdk.nauta.network.HttpResponse;
import cu.suitetecsa.sdk.nauta.scraper.AccountParser;
import cu.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;
import cu.suitetecsa.sdk.nauta.utils.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class JsoupAccountParser implements AccountParser {

    @Contract("_ -> new")
    private @NotNull AccountDetail parseAccountAttributes(@NotNull Document htmlParsed) throws ParseException {
        List<String> attrs = htmlParsed.selectFirst(".z-depth-1").select(".m6")
                .stream().map(attr -> attr.selectFirst("p").text().trim())
                .toList();
        try {
            return new AccountDetail(
                    attrs.get(0),
                    StringUtils.toDateMillis(attrs.get(1)),
                    StringUtils.toDateMillis(attrs.get(2)),
                    attrs.get(3),
                    attrs.get(4),
                    Double.parseDouble(attrs.get(5).replace("$", "").replace(" CUP", "").replace(",", ".")),
                    StringUtils.toSeconds(attrs.get(6)),
                    attrs.get(7),
                    attrs.size() > 8 ? attrs.get(8) : null,
                    attrs.size() > 9 ? Double.parseDouble(attrs.get(9).replace("$", "").replace(" CUP", "").replace(",", ".")) : null,
                    attrs.size() > 10 ? attrs.get(10) : null,
                    attrs.size() > 11 ? attrs.get(11) : null,
                    attrs.size() > 12 ? attrs.get(12) : null,
                    attrs.size() > 13 ? attrs.get(13) : null,
                    attrs.size() > 14 ? attrs.get(14) : null,
                    attrs.size() > 15 ? StringUtils.toDateMillis(attrs.get(15)) : null,
                    attrs.size() > 16 ? StringUtils.toDateMillis(attrs.get(16)) : null,
                    attrs.size() > 17 ? StringUtils.toDateMillis(attrs.get(17)) : null,
                    attrs.size() > 18 ? Double.parseDouble(attrs.get(18).replace("$", "").replace(" CUP", "").replace(",", ".")) : null,
                    attrs.size() > 19 ? Double.parseDouble(attrs.get(19).replace("$", "").replace(" CUP", "").replace(",", ".")) : null,
                    attrs.size() > 20 ? Double.parseDouble(attrs.get(20).replace("$", "").replace(" CUP", "").replace(",", ".")) : null
            );
        } catch (InvalidSessionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AccountDetail parseAccount(HttpResponse httpResponse) throws NautaGetInfoException, NotLoggedInException, ParseException {
        throwExceptionOnFailure(httpResponse, "Fail to parse user details", new ExceptionHandler<>(NautaGetInfoException::new));
        return parseAccountAttributes(Jsoup.parse(httpResponse.getText()));
    }

    @Override
    public <T extends Exception> HttpResponse throwExceptionOnFailure(HttpResponse httpResponse, String message, ExceptionHandler<T> exceptionHandler) throws T, NotLoggedInException {
        return JsoupErrorParser.whenPortalManager(PortalManager.USER).throwExceptionOnFailure(httpResponse, message, exceptionHandler);
    }
}
