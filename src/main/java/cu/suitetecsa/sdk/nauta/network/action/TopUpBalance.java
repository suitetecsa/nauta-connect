package cu.suitetecsa.sdk.nauta.network.action;

import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.ActionType;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;

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
    public int getCount() {
        return 0;
    }

    @Override
    public String getYearMonthSelected() {
        return null;
    }

    @Override
    public int getPagesCount() {
        return 0;
    }

    @Override
    public int getLarge() {
        return 0;
    }

    @Override
    public boolean isReversed() {
        return false;
    }

    @Override
    public ActionType getType() {
        return null;
    }
}
