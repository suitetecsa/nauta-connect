package cu.suitetecsa.sdk.nauta.scraper;

import cu.suitetecsa.sdk.nauta.exception.NotLoggedInException;
import cu.suitetecsa.sdk.nauta.network.HttpResponse;
import cu.suitetecsa.sdk.nauta.utils.Constants;
import cu.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ErrorParserImpl implements ErrorParser {
    private final PortalManager portalManager;

    private Pattern getRegex() {
        return switch (portalManager) {
            case CONNECT -> Pattern.compile("alert\\(\"(?<reason>[^\"]*?)\"\\)");
            case USER -> Pattern.compile("toastr\\.error\\('(?<reason>.*)'\\)");
        };
    }

    ErrorParserImpl(PortalManager portalManager) {
        this.portalManager = portalManager;
    }

    private String getErrorMessage(Document html) {
        return parseError(html);
    }

    private static List<String> parseErrors(@NotNull String errorMessage) {
        if (errorMessage.startsWith(Constants.variousErrorsDetected)) {
            Document errorsHtml = Jsoup.parse(errorMessage);
            Elements subMessages = errorsHtml.select("li[class='sub-message']");
            return subMessages.stream().map(Element::text).toList();
        } else {
            return List.of(errorMessage);
        }
    }

    /**
     * Parses the error message from the given HTML document.
     *
     * @param document The HTML document to parse.
     * @return The parsed error message, or null if no error message is found.
     */
    private @Nullable String parseError(@NotNull Document document) {
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

    @Override
    public <T extends Exception> HttpResponse throwExceptionOnFailure(@NotNull HttpResponse httpResponse, String message, ExceptionHandler<T> exceptionHandler) throws T, NotLoggedInException {
        String errorMessage = getErrorMessage(Jsoup.parse(httpResponse.getText()));
        if (errorMessage != null) {
            List<String> errors = parseErrors(errorMessage);

            if (errors.size() == 0 && errorMessage.contains(Constants.variousErrorsDetected)) {
                throw exceptionHandler.handleException(message, List.of(errorMessage.replace(Constants.variousErrorsDetected + " ", "")));
            } else if (errors.size() == 1 && Constants.notLoggedInErrors.contains(errors.get(0))) {
                throw new ExceptionHandler<>(NotLoggedInException::new)
                        .handleException(message, errors);
            }

            throw exceptionHandler.handleException(message, errors);
        }
        return httpResponse;
    }
}
