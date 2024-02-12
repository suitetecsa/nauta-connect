package io.github.suitetecsa.sdk.nauta.scraper;

import io.github.suitetecsa.sdk.nauta.exception.NautaGetInfoException;
import io.github.suitetecsa.sdk.nauta.exception.NotLoggedInException;
import io.github.suitetecsa.sdk.nauta.model.AccountDetail;
import io.github.suitetecsa.sdk.nauta.network.HttpResponse;

import java.text.ParseException;

public interface AccountParser extends ErrorParser {
    AccountDetail parseAccount(HttpResponse httpResponse) throws NautaGetInfoException, NotLoggedInException, ParseException;

    class Builder {
        public AccountParser build() {
            return new AccountParserImpl();
        }
    }
}
