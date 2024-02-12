package io.github.suitetecsa.sdk.nauta.utils;

public enum PortalManager {
    CONNECT("https://secure.etecsa.net:8443"),
    USER("https://www.portal.nauta.cu");

    private final String baseUrl;

    private PortalManager(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}

