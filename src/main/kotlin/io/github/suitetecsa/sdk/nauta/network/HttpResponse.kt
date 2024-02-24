package io.github.suitetecsa.sdk.nauta.network

import org.jetbrains.annotations.Contract
import java.nio.charset.StandardCharsets

class HttpResponse(
    val statusCode: Int,
    val statusMessage: String,
    content: ByteArray,
    val cookies: Map<String, String>
) {
    @Contract(value = " -> new", pure = true)
    fun content(): ByteArray {
        return content.copyOf(content.size)
    }

    @get:Contract(value = " -> new", pure = true)
    val text: String
        get() = String(content, StandardCharsets.UTF_8)
    val content: ByteArray = content.copyOf(content.size)
}