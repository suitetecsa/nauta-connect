package cu.suitetecsa.sdk.nauta.network;

import java.util.Arrays;
import java.util.Map;

/**
 * Clase que representa una respuesta HTTP.
 */
public class HttpResponse {
    private final int statusCode;
    private final String statusMessage;
    private final byte[] content;
    private final Map<String, String> cookies;

    public HttpResponse(int statusCode, String statusMessage, byte[] content, Map<String, String> cookies) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.content = Arrays.copyOf(content, content.length);
        this.cookies = cookies;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public byte[] getContent() {
        return Arrays.copyOf(content, content.length);
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public String getText() {
        return new String(content, java.nio.charset.StandardCharsets.UTF_8);
    }
}
