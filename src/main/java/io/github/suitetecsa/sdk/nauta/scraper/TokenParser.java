package io.github.suitetecsa.sdk.nauta.scraper;

import io.github.suitetecsa.sdk.nauta.network.HttpResponse;

public interface TokenParser {
    String parseCsrfToken(HttpResponse httpResponse);

    class Builder {
        public TokenParser build() {
            return new TokenParserImpl();
        }
    }
}
