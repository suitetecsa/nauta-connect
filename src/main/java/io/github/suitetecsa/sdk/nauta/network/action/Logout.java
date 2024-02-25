package io.github.suitetecsa.sdk.nauta.network.action;

import io.github.suitetecsa.sdk.nauta.network.Action;
import io.github.suitetecsa.sdk.nauta.network.HttpMethod;
import io.github.suitetecsa.sdk.nauta.utils.PortalManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Logout implements Action {
    private final String username;
    private final String wlanUserIp;
    private final String csrfHw;
    private final String attributeUUID;

    public Logout(@NotNull String username, @NotNull String wlanUserIp, @NotNull String csrfHw, @NotNull String attributeUUID) {
        this.username = username;
        this.wlanUserIp = wlanUserIp;
        this.csrfHw = csrfHw;
        this.attributeUUID = attributeUUID;
    }

    @Override
    public String getUrl() {
        return PortalManager.CONNECT.getBaseUrl() + "/LogoutServlet";
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("wlanuserip", wlanUserIp);
        data.put("CSRFHW", csrfHw);
        data.put("ATTRIBUTE_UUID", attributeUUID);
        return data;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
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
