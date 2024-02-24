package io.github.suitetecsa.sdk.nauta.scraper

import io.github.suitetecsa.sdk.nauta.network.HttpResponse
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*

internal class FormParserImpl : FormParser {
    private fun getHtml(httpResponse: HttpResponse): Document {
        return Jsoup.parse(httpResponse.text)
    }

    override fun parseActionForm(httpResponse: HttpResponse): AbstractMap.SimpleEntry<String, Map<String, String>> {
        return parseForm(getHtml(httpResponse).selectFirst("form[action]"))!!
    }

    override fun parseLoginForm(httpResponse: HttpResponse): AbstractMap.SimpleEntry<String, Map<String, String>> {
        return parseForm(getHtml(httpResponse).selectFirst("form.form"))!!
    }

    private fun parseForm(form: Element?): AbstractMap.SimpleEntry<String, Map<String, String>>? {
        if (form != null) {
            val data = getInputs(form)
            val url = form.attr("action")
            return AbstractMap.SimpleEntry(url, data)
        }
        return null
    }

    private fun getInputs(form: Element): HashMap<String, String> {
        val inputs = HashMap<String, String>()
        for (input in form.select("input[name]")) {
            inputs[input.attr("name")] = input.attr("value")
        }
        return inputs
    }
}
