package io.github.suitetecsa.sdk.nauta.scraper;

import io.github.suitetecsa.sdk.nauta.exception.InvalidSessionException;
import io.github.suitetecsa.sdk.nauta.exception.NautaGetInfoException;
import io.github.suitetecsa.sdk.nauta.exception.NotLoggedInException;
import io.github.suitetecsa.sdk.nauta.model.AccountDetail;
import io.github.suitetecsa.sdk.nauta.network.HttpResponse;
import io.github.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import io.github.suitetecsa.sdk.nauta.utils.PortalManager;
import io.github.suitetecsa.sdk.nauta.utils.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.ParseException;
import java.util.List;

class AccountParserImpl implements AccountParser {

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
                    StringUtils.fromPriceString(attrs.get(5)),
                    StringUtils.toSeconds(attrs.get(6)),
                    attrs.get(7),
                    attrs.size() > 8 ? attrs.get(8) : null,
                    attrs.size() > 9 ? StringUtils.fromPriceString(attrs.get(9)) : null,
                    attrs.size() > 10 ? attrs.get(10) : null,
                    attrs.size() > 11 ? attrs.get(11) : null,
                    attrs.size() > 12 ? attrs.get(12) : null,
                    attrs.size() > 13 ? attrs.get(13) : null,
                    attrs.size() > 14 ? attrs.get(14) : null,
                    attrs.size() > 15 ? StringUtils.toDateMillis(attrs.get(15)) : null,
                    attrs.size() > 16 ? StringUtils.toDateMillis(attrs.get(16)) : null,
                    attrs.size() > 17 ? StringUtils.toDateMillis(attrs.get(17)) : null,
                    attrs.size() > 18 ? StringUtils.fromPriceString(attrs.get(18)) : null,
                    attrs.size() > 19 ? StringUtils.fromPriceString(attrs.get(19)) : null,
                    attrs.size() > 20 ? StringUtils.fromPriceString(attrs.get(20)) : null
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
        return new ErrorParser.Builder().whenPortalManager(PortalManager.USER).build().throwExceptionOnFailure(httpResponse, message, exceptionHandler);
    }
}
