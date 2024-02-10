package cu.suitetecsa.sdk.nauta.utils;

import cu.suitetecsa.sdk.nauta.exception.NautaException;
import cu.suitetecsa.sdk.nauta.exception.NotLoggedInException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class DocumentUtils {
    private static String getErrorMessage(Document html, PortalManager portalManager) {
        return new HtmlErrorParser().whenPortalManager(portalManager).parseError(html);
    }

    private static List<String> parseErrors(String errorMessage) {
        if (errorMessage.startsWith("Se han detectado algunos errores.")) {
            Document errorsHtml = Jsoup.parse(errorMessage);
            Elements subMessages = errorsHtml.select("li[class='sub-message']");
            return subMessages.stream().map(Element::text).toList();
        } else {
            return List.of(errorMessage);
        }
    }

    public static void throwExceptionOnFailure(Document html, String message, PortalManager portalManager) throws NotLoggedInException, NautaException {
        String errorMessage = getErrorMessage(html, portalManager);
        if (errorMessage != null) {
            List<String> errors = parseErrors(errorMessage);

            if (errors.size() == 1 && Constants.notLoggedInErrors.contains(errors.get(0))) {
                throw new ExceptionHandler.Builder<NotLoggedInException>()
                        .build()
                        .handleException(message, errors);
            }

            throw new ExceptionHandler.Builder<NautaException>().build().handleException(message, errors);
        }
    }

    public static <T extends Exception> void throwExceptionOnFailure(Document html, String message, PortalManager portalManager, ExceptionHandler<T> exceptionHandler) throws NautaException, T {
        String errorMessage = getErrorMessage(html, portalManager);
        if (errorMessage != null) {
            List<String> errors = parseErrors(errorMessage);

            if (errors.size() == 1 && Constants.notLoggedInErrors.contains(errors.get(0))) {
                throw new ExceptionHandler.Builder<NautaException>()
                        .build()
                        .handleException(message, errors);
            }

            throw exceptionHandler.handleException(message, errors);
        }
    }
}
