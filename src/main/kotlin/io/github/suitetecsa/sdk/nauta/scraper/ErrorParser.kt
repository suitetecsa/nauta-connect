package io.github.suitetecsa.sdk.nauta.scraper

import io.github.suitetecsa.sdk.nauta.exception.NotLoggedInException
import io.github.suitetecsa.sdk.nauta.network.HttpResponse
import io.github.suitetecsa.sdk.nauta.utils.ExceptionHandler
import io.github.suitetecsa.sdk.nauta.utils.PortalManager

interface ErrorParser {
    @Throws(NotLoggedInException::class)
    fun <T : Exception> throwExceptionOnFailure(
        httpResponse: HttpResponse,
        message: String,
        exceptionHandler: ExceptionHandler<T>
    ): HttpResponse

    class Builder {
        private var portalManager: PortalManager? = null

        fun whenPortalManager(portalManager: PortalManager): Builder {
            this.portalManager = portalManager
            return this
        }

        fun build(): ErrorParser {
            return ErrorParserImpl(portalManager ?: PortalManager.USER)
        }
    }
}
