package cu.suitetecsa.sdk.nauta.scraper;

import cu.suitetecsa.sdk.nauta.exception.NautaGetInfoException;
import cu.suitetecsa.sdk.nauta.exception.NotLoggedInException;
import cu.suitetecsa.sdk.nauta.model.AccountDetail;
import cu.suitetecsa.sdk.nauta.network.HttpResponse;

import java.text.ParseException;

public interface AccountParser extends ErrorParser {
    AccountDetail parseAccount(HttpResponse httpResponse) throws NautaGetInfoException, NotLoggedInException, ParseException;

    class Builder {
        public AccountParser build() {
            return new AccountParserImpl();
        }
    }
}
