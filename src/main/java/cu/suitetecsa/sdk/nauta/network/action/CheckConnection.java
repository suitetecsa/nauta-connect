package cu.suitetecsa.sdk.nauta.network.action;

import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.ActionType;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;

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

