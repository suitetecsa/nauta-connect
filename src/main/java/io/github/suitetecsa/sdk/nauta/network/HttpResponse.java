package io.github.suitetecsa.sdk.nauta.network;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public record HttpResponse(int statusCode, String statusMessage, byte[] content, Map<String, String> cookies) {
    public HttpResponse(int statusCode, String statusMessage, byte[] content, Map<String, String> cookies) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.content = Arrays.copyOf(content, content.length);
        this.cookies = cookies;
    }

    @Override
    @Contract(value = " -> new", pure = true)
    public byte @NotNull [] content() {
        return Arrays.copyOf(content, content.length);
    }

    @Contract(value = " -> new", pure = true)
    public @NotNull String getText() {
        return new String(content, StandardCharsets.UTF_8);
    }
}