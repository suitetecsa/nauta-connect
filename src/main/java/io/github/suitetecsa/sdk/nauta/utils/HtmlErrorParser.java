package io.github.suitetecsa.sdk.nauta.utils;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlErrorParser {
    private PortalManager portalManager = PortalManager.USER;
    private Pattern getRegex() {
        return switch (portalManager) {
            case CONNECT -> Pattern.compile("alert\\(\"(?<reason>[^\"]*?)\"\\)");
            case USER -> Pattern.compile("toastr\\.error\\('(?<reason>.*)'\\)");
        };
    }

    public HtmlErrorParser whenPortalManager(PortalManager portalManager) {
        this.portalManager = portalManager;
        return this;
    }

    /**
     * Parses the error message from the given HTML document.
     *
     * @param document The HTML document to parse.
     * @return The parsed error message, or null if no error message is found.
     */
    public String parseError(@NotNull Document document) {
        Elements scripts = document.select("script[type='text/javascript']");
        Element lastScript = scripts.last();

        if (lastScript != null) {
            String scriptData = lastScript.data().trim();
            Matcher matcher = getRegex().matcher(scriptData);

            if (matcher.find()) {
                String reason = matcher.group("reason");
                if (reason != null) {
                    Element errorMsg = Jsoup.parse(reason).selectFirst("li[class=\"msg_error\"]");
                    if (errorMsg != null) {
                        return errorMsg.text();
                    }
                }
            }
        }

        return null;
    }
}
