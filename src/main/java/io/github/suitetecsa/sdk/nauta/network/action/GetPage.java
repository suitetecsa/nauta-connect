package io.github.suitetecsa.sdk.nauta.network.action;

import io.github.suitetecsa.sdk.nauta.network.Action;
import io.github.suitetecsa.sdk.nauta.network.HttpMethod;

import java.util.Map;

public class GetPage implements Action {
    private final String url;
    private final Map<String, String> data;
    private final HttpMethod method;

    public GetPage(String url, Map<String, String> data, HttpMethod method) {
        this.url = url;
        this.data = data;
        this.method = method;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Map<String, String> getData() {
        return data;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public boolean isIgnoreContentType() {
        return false;
    }

    @Override
    public int getTimeout() {
        return TIMEOUT_MS;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public String yearMonthSelected() {
        return null;
    }

}
