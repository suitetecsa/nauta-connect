package cu.suitetecsa.sdk.nauta.jsoupimpl;

import cu.suitetecsa.sdk.nauta.network.HttpResponse;
import cu.suitetecsa.sdk.nauta.scraper.TokenParser;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;

import javax.swing.*;

public class JsoupTokenParser implements TokenParser {
    @Override
    public String parseCsrfToken(@NotNull HttpResponse httpResponse) {
        System.out.println(httpResponse.getText());
        return Jsoup.parse(httpResponse.getText()).selectFirst("input[name=csrf]").attr("value");
    }
}
