package cu.suitetecsa.sdk.nauta.network.action;

import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.ActionType;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;

import java.util.Map;

public class GetActions implements Action {
    private final int count;
    private final String yearMonthSelected;
    private final int pagesCount;
    private final boolean reversed;
    private final ActionType type;

    public GetActions(int count, String yearMonthSelected, int pagesCount, boolean reversed, ActionType type) {
        this.count = count;
        this.yearMonthSelected = yearMonthSelected;
        this.pagesCount = pagesCount;
        this.reversed = reversed;
        this.type = type;
    }

    @Override
    public String getUrl() {
        String baseUrl = PortalManager.USER.getBaseUrl();
        String actionPath = switch (type) {
            case Connections -> "/useraaa/service_detail_list/";
            case Recharges -> "/useraaa/recharge_detail_list/";
            case Transfers -> "/useraaa/transfer_detail_list/";
            case QuotesPaid -> "/useraaa/nautahogarpaid_detail_list/";
        };
        return baseUrl + actionPath;
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
    public String getCsrfUrl() {
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public String getYearMonthSelected() {
        return yearMonthSelected;
    }

    @Override
    public int getPagesCount() {
        return pagesCount;
    }

    @Override
    public int getLarge() {
        return 0;
    }

    @Override
    public boolean isReversed() {
        return reversed;
    }

    @Override
    public ActionType getType() {
        return type;
    }
}
