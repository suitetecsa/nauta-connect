package cu.suitetecsa.sdk.nauta.network.action;

import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.ActionType;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;

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
