package io.github.suitetecsa.sdk.nauta.scraper;

import io.github.suitetecsa.sdk.nauta.network.HttpResponse;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;

class TokenParserImpl implements TokenParser {
    @Override
    public String parseCsrfToken(@NotNull HttpResponse httpResponse) {
        return Jsoup.parse(httpResponse.getText()).selectFirst("input[name=csrf]").attr("value");
    }
}
