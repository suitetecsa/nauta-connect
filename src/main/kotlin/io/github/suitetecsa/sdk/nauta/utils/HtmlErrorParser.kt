package io.github.suitetecsa.sdk.nauta.utils

import org.jetbrains.annotations.Contract
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.regex.Pattern

class HtmlErrorParser private constructor(private val portalManager: PortalManager) {
    private val regex: Pattern
        get() = when (portalManager) {
            PortalManager.CONNECT -> Pattern.compile("alert\\(\"(?<reason>[^\"]*?)\"\\)")
            PortalManager.USER -> Pattern.compile("toastr\\.error\\('(?<reason>.*)'\\)")
        }

    /**
     * Parses the error message from the given HTML document.
     *
     * @param document The HTML document to parse.
     * @return The parsed error message, or null if no error message is found.
     */
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

    companion object {
        @JvmStatic
        @Contract(value = "_ -> new", pure = true)
        fun whenPortalManager(portalManager: PortalManager): HtmlErrorParser {
            return HtmlErrorParser(portalManager)
        }
    }
}
