package io.github.suitetecsa.sdk.nauta.scraper;

import io.github.suitetecsa.sdk.nauta.exception.NotLoggedInException;
import io.github.suitetecsa.sdk.nauta.network.HttpResponse;
import io.github.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import io.github.suitetecsa.sdk.nauta.utils.PortalManager;

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
