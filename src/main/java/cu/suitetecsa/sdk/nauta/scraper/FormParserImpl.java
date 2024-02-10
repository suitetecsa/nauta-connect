package cu.suitetecsa.sdk.nauta.scraper;

import cu.suitetecsa.sdk.nauta.network.HttpResponse;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

public class JsoupFormParser implements FormParser {

    private Document getHtml(@NotNull HttpResponse httpResponse) {
        return Jsoup.parse(httpResponse.getText());
    }
    @Override
    public SimpleEntry<String, Map<String, String>> parseActionForm(@NotNull HttpResponse httpResponse) {
        return parseForm(getHtml(httpResponse).selectFirst("form[action]"));
    }

    @Override
    public SimpleEntry<String, Map<String, String>> parseLoginForm(@NotNull HttpResponse httpResponse) {
        return parseForm(getHtml(httpResponse).selectFirst("form.form"));
    }

    SimpleEntry<String, Map<String, String>> parseForm(Element form) {
        if (form != null) {
            HashMap<String, String> data = getInputs(form);
            String url = form.attr("action");
            return new SimpleEntry<>(url, data);
        }
        return null;
    }

    HashMap<String, String> getInputs(@NotNull Element form) {
        HashMap<String, String> inputs = new HashMap<>();
        for (Element input : form.select("input[name]")) {
            inputs.put(input.attr("name"), input.attr("value"));
        }
        return inputs;
    }
}
