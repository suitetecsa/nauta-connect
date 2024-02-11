package cu.suitetecsa.sdk.nauta.scraper;

import cu.suitetecsa.sdk.nauta.exception.NautaException;
import cu.suitetecsa.sdk.nauta.exception.NotLoggedInException;
import cu.suitetecsa.sdk.nauta.network.HttpResponse;
import cu.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;

public interface ErrorParser {
    <T extends Exception> HttpResponse throwExceptionOnFailure(HttpResponse httpResponse, String message, ExceptionHandler<T> exceptionHandler) throws T, NotLoggedInException;

    class Builder {
        private PortalManager portalManager;

        public Builder whenPortalManager(PortalManager portalManager) {
            this.portalManager = portalManager;
            return this;
        }
        public ErrorParser build() {
            return new ErrorParserImpl(portalManager != null ? portalManager : PortalManager.USER);
        }
    }
}
