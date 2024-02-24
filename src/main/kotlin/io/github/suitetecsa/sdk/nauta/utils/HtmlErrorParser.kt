package io.github.suitetecsa.sdk.nauta.utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.regex.Pattern

object HtmlErrorParser {
    private val regex: Pattern
        get() = Pattern.compile("alert\\(\"(?<reason>[^\"]*?)\"\\)")

    /**
     * Parses the error message from the given HTML document.
     *
     * @param document The HTML document to parse.
     * @return The parsed error message, or null if no error message is found.
     */
    @JvmStatic
    fun parseError(document: Document): String? {
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
}
