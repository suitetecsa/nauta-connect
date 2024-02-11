package cu.suitetecsa.sdk.nauta.scraper;

import cu.suitetecsa.sdk.nauta.network.HttpResponse;

public interface TokenParser {
    String parseCsrfToken(HttpResponse httpResponse);

    class Builder {
        public TokenParser build() {
            return new TokenParserImpl();
        }
    }
}
