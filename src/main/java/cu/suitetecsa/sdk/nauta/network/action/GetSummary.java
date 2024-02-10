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

    @Override
    public String getUrl() {
        return switch (type) {
            case Connections -> "/useraaa/service_detail_summary/";
            case Recharges -> "/useraaa/recharge_detail_summary/";
            case Transfers -> "/useraaa/transfer_detail_summary/";
            case QuotesPaid -> "/useraaa/nautahogarpaid_detail_summary/";
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
            case QuotesPaid -> "nautahogarpaid_detail";
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
            case Connections -> "/useraaa/service_detail/";
            case Recharges -> "/useraaa/recharge_detail/";
            case Transfers -> "/useraaa/transfer_detail/";
            case QuotesPaid -> "/useraaa/nautahogarpaid_detail/";
        };
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
        return type;
    }
}
