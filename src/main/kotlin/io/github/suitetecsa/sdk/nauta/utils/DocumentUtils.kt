package io.github.suitetecsa.sdk.nauta.utils

import io.github.suitetecsa.sdk.nauta.exception.NautaException
import io.github.suitetecsa.sdk.nauta.exception.NotLoggedInException
import io.github.suitetecsa.sdk.nauta.utils.HtmlErrorParser.Companion.whenPortalManager
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

object DocumentUtils {
    private fun getErrorMessage(html: Document, portalManager: PortalManager): String? {
        return whenPortalManager(portalManager).parseError(html)
    }

    private fun parseErrors(errorMessage: String): List<String> {
        if (errorMessage.startsWith("Se han detectado algunos errores.")) {
            val errorsHtml = Jsoup.parse(errorMessage)
            val subMessages = errorsHtml.select("li[class='sub-message']")
            return subMessages.stream().map { obj: Element -> obj.text() }.toList()
        } else {
            return listOf(errorMessage)
        }
    }

    @Throws(NotLoggedInException::class, NautaException::class)
    fun throwExceptionOnFailure(html: Document, message: String?, portalManager: PortalManager) {
        val errorMessage = getErrorMessage(html, portalManager)
        if (errorMessage != null) {
            val errors = parseErrors(errorMessage)

            if (errors.size == 1 && Constants.notLoggedInErrors.contains(errors[0])) {
                throw ExceptionHandler.Builder<NotLoggedInException>()
                    .build()
                    .handleException(message!!, errors)
            }

            throw ExceptionHandler.Builder<NautaException>().build().handleException(
                message!!, errors
            )
        }
    }

    @Throws(NautaException::class)
    fun <T : Exception> throwExceptionOnFailure(
        html: Document,
        message: String?,
        portalManager: PortalManager,
        exceptionHandler: ExceptionHandler<T>
    ) {
        val errorMessage = getErrorMessage(html, portalManager)
        if (errorMessage != null) {
            val errors = parseErrors(errorMessage)

            if (errors.size == 1 && Constants.notLoggedInErrors.contains(errors[0])) {
                throw ExceptionHandler.Builder<NautaException>()
                    .build()
                    .handleException(message!!, errors)
            }

            throw exceptionHandler.handleException(message!!, errors)
        }
    }
}
