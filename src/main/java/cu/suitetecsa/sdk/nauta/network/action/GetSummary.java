package cu.suitetecsa.sdk.nauta.network.action;

import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.ActionType;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class GetSummary implements Action {
    private final String csrf;
    private final int year;
    private final int month;
    private final ActionType type;
    private final HttpMethod method;

    public GetSummary(String csrf, int year, int month, ActionType type, HttpMethod method) {
        this.csrf = csrf;
        this.year = year;
        this.month = month;
        this.type = type;
        this.method = method;
    }

    public GetSummary copyWithCsrfAndMethod(String csrf, HttpMethod method) {
        return new GetSummary(csrf, this.year, this.month, this.type, method);
    }

    @Override
    public String getUrl() {
        return switch (type) {
            case Connections -> "https://www.portal.nauta.cu/useraaa/service_detail_summary/";
            case Recharges -> "https://www.portal.nauta.cu/useraaa/recharge_detail_summary/";
            case Transfers -> "https://www.portal.nauta.cu/useraaa/transfer_detail_summary/";
            case FeesPaid -> "https://www.portal.nauta.cu/useraaa/nautahogarpaid_detail_summary/";
        };
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("csrf", csrf != null ? csrf : "");
        data.put("year_month", year + "-" + String.format("%02d", month));
        data.put("list_type", getListType());
        return data;
    }

    private String getListType() {
        return switch (type) {
            case Connections -> "service_detail";
            case Recharges -> "recharge_detail";
            case Transfers -> "transfer_detail";
            case FeesPaid -> "nautahogarpaid_detail";
        };
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
        return switch (type) {
            case Connections -> "https://www.portal.nauta.cu/useraaa/service_detail/";
            case Recharges -> "https://www.portal.nauta.cu/useraaa/recharge_detail/";
            case Transfers -> "https://www.portal.nauta.cu/useraaa/transfer_detail/";
            case FeesPaid -> "https://www.portal.nauta.cu/useraaa/nautahogarpaid_detail/";
        };
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
        return type;
    }
}
