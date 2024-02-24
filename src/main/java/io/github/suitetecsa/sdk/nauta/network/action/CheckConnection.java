package io.github.suitetecsa.sdk.nauta.network.action;

import io.github.suitetecsa.sdk.nauta.network.Action;
import io.github.suitetecsa.sdk.nauta.network.HttpMethod;

import java.util.Map;

public class CheckConnection implements Action {
    private final String checkUrl;

    public CheckConnection(String checkUrl) {
        this.checkUrl = checkUrl;
    }

    @Override
    public String getUrl() {
        return checkUrl != null ? checkUrl : "http://www.cubadebate.cu/";
    }

    @Override
    public Map<String, String> getData() {
        return null;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
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

