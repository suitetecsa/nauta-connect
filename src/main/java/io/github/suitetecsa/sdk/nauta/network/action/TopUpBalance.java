package io.github.suitetecsa.sdk.nauta.network.action;

import io.github.suitetecsa.sdk.nauta.network.Action;
import io.github.suitetecsa.sdk.nauta.network.ActionType;
import io.github.suitetecsa.sdk.nauta.network.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class TopUpBalance implements Action {
    private final String csrf;
    private final String rechargeCode;
    private final HttpMethod method;

    public TopUpBalance(String csrf, String rechargeCode, HttpMethod method) {
        this.csrf = csrf;
        this.rechargeCode = rechargeCode;
        this.method = method;
    }

    public TopUpBalance copyWithCsrfAndMethod(String csrf, HttpMethod method) {
        return new TopUpBalance(csrf, this.rechargeCode, method);
    }

    @Override
    public String getUrl() {
        return "/useraaa/recharge_account";
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("csrf", csrf != null || method == HttpMethod.POST ? csrf : "");
        data.put("recharge_code", rechargeCode);
        data.put("btn_submit", "");
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
    public String getCsrfUrl() {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public String yearMonthSelected() {
        return null;
    }

    @Override
    public int pagesCount() {
        return 0;
    }

    @Override
    public int getLarge() {
        return 0;
    }

    @Override
    public boolean reversed() {
        return false;
    }

    @Override
    public ActionType type() {
        return null;
    }
}
