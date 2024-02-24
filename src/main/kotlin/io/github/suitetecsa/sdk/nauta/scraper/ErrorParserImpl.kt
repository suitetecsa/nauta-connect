package io.github.suitetecsa.sdk.nauta.scraper

import io.github.suitetecsa.sdk.nauta.exception.NotLoggedInException
import io.github.suitetecsa.sdk.nauta.network.HttpResponse
import io.github.suitetecsa.sdk.nauta.utils.Constants
import io.github.suitetecsa.sdk.nauta.utils.ExceptionHandler
import io.github.suitetecsa.sdk.nauta.utils.PortalManager
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.regex.Pattern

internal class ErrorParserImpl(private val portalManager: PortalManager) : ErrorParser {
    private val regex: Pattern
        get() = when (portalManager) {
            PortalManager.CONNECT -> Pattern.compile("alert\\(\"(?<reason>[^\"]*?)\"\\)")
            PortalManager.USER -> Pattern.compile("toastr\\.error\\('(?<reason>.*)'\\)")
        }

    private fun getErrorMessage(html: Document): String? {
        return parseError(html)
    }

    /**
     * Parses the error message from the given HTML document.
     *
     * @param document The HTML document to parse.
     * @return The parsed error message, or null if no error message is found.
     */
    private fun parseError(document: Document): String? {
        val scripts = document.select("script[type='text/javascript']")
        val lastScript = scripts.last()

        if (lastScript != null) {
            val scriptData = lastScript.data().trim { it <= ' ' }
            val matcher = regex.matcher(scriptData)

            if (matcher.find()) {
                val reason = matcher.group("reason")
                if (reason != null) {
                    val errorMsg = Jsoup.parse(reason).selectFirst("li[class=\"msg_error\"]")
                    if (errorMsg != null) {
                        return errorMsg.text()
                    }
                }
            }
        }

        return null
    }

    @Throws(NotLoggedInException::class)
    override fun <T : Exception> throwExceptionOnFailure(
        httpResponse: HttpResponse,
        message: String,
        exceptionHandler: ExceptionHandler<T>
    ): HttpResponse {
        val errorMessage = getErrorMessage(Jsoup.parse(httpResponse.text))
        if (errorMessage != null) {
            val errors = parseErrors(errorMessage)

            if (errors.isEmpty() && errorMessage.contains(Constants.VARIOUS_ERRORS_DETECTED)) {
                throw exceptionHandler.handleException(
                    message,
                    listOf(errorMessage.replace(Constants.VARIOUS_ERRORS_DETECTED + " ", ""))
                )
            } else if (errors.size == 1 && Constants.notLoggedInErrors.contains(
                    errors[0]
                )
            ) {
                throw ExceptionHandler { NotLoggedInException(it) }
                    .handleException(message, errors)
            }

            throw exceptionHandler.handleException(message, errors)
        }
        return httpResponse
    }

    companion object {
        private fun parseErrors(errorMessage: String): List<String> {
            if (errorMessage.startsWith(Constants.VARIOUS_ERRORS_DETECTED)) {
                val errorsHtml = Jsoup.parse(errorMessage)
                val subMessages = errorsHtml.select("li[class='sub-message']")
                return subMessages.stream().map { obj: Element -> obj.text() }.toList()
            } else {
                return listOf(errorMessage)
            }
        }
    }
}
