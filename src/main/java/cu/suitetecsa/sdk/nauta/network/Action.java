package cu.suitetecsa.sdk.nauta.network;

import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException;

import java.util.Map;

public interface Action {
    String getUrl();
    Map<String, String> getData() throws NautaAttributeException;
    HttpMethod getMethod();
    boolean isIgnoreContentType();
    int getTimeout();
    String getCsrfUrl();
    int getCount();
    String getYearMonthSelected();
    int getPagesCount();
    int getLarge();
    boolean isReversed();
    ActionType getType();

    int TIMEOUT_MS = 30000;
}
