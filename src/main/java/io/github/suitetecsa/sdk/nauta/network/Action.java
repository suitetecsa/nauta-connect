package io.github.suitetecsa.sdk.nauta.network;

import io.github.suitetecsa.sdk.nauta.exception.NautaAttributeException;

import java.util.Map;

public interface Action {
    String getUrl();
    Map<String, String> getData() throws NautaAttributeException;
    HttpMethod getMethod();
    boolean isIgnoreContentType();
    int getTimeout();

    int count();
    String yearMonthSelected();

    int TIMEOUT_MS = 30000;
}
