package io.github.suitetecsa.sdk.nauta.utils

import io.github.suitetecsa.sdk.nauta.exception.NautaException
import io.github.suitetecsa.sdk.nauta.exception.NotLoggedInException
import io.github.suitetecsa.sdk.nauta.utils.HtmlErrorParser.parseError
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object DocumentUtils {
    private fun getErrorMessage(html: Document): String? {
        return parseError(html)
    }

    private fun parseErrors(errorMessage: String): List<String> {
        if (errorMessage.startsWith("Se han detectado algunos errores.")) {
            val errorsHtml = Jsoup.parse(errorMessage)
            val subMessages = errorsHtml.select("li[class='sub-message']")
            return subMessages.map { it.text() }
        } else {
            return listOf(errorMessage)
        }
    }

    /**
     * Throws a NotLoggedInException or NautaException if an error is detected in the HTML.
     *
     * @param html The HTML document to check for errors
     * @param message The error message to use if throwing an exception
     * @throws NotLoggedInException If a not logged in error is detected
     * @throws NautaException For any other errors detected
     */
    @JvmStatic
    @Throws(NotLoggedInException::class, NautaException::class)
    fun throwExceptionOnFailure(html: Document, message: String) = getErrorMessage(html)?.let {
            val errors = parseErrors(it)

            if (errors.size == 1 && Constants.notLoggedInErrors.contains(errors[0])) {
                throw ExceptionHandler.Builder<NotLoggedInException>()
                    .build()
                    .handleException(message, errors)
            }

            throw ExceptionHandler.Builder<NautaException>().build().handleException(
                message, errors
            )
        }

    @JvmStatic
    @Throws(NautaException::class)
    fun <T : Exception> throwExceptionOnFailure(
        html: Document,
        message: String?,
        exceptionHandler: ExceptionHandler<T>
    ) {
        val errorMessage = getErrorMessage(html)
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
