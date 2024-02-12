package io.github.suitetecsa.sdk.nauta.network.action;

import io.github.suitetecsa.sdk.nauta.exception.NautaAttributeException;
import io.github.suitetecsa.sdk.nauta.network.Action;
import io.github.suitetecsa.sdk.nauta.network.ActionType;
import io.github.suitetecsa.sdk.nauta.network.HttpMethod;
import io.github.suitetecsa.sdk.nauta.utils.PortalManager;

import java.util.Map;

public class LoadUserInformation implements Action {
    private final String username;
    private final String password;
    private final String wlanUserIp;
    private final String csrfHw;
    private final String attributeUUID;
    private final PortalManager portal;

    public LoadUserInformation(String username, String password, String wlanUserIp, String csrfHw, String attributeUUID, PortalManager portal) {
        this.username = username;
        this.password = password;
        this.wlanUserIp = wlanUserIp;
        this.csrfHw = csrfHw;
        this.attributeUUID = attributeUUID;
        this.portal = portal;
    }

    @Override
    public String getUrl() {
        return switch (portal) {
            case CONNECT -> portal.getBaseUrl() + "/EtecsaQueryServlet";
            case USER -> portal.getBaseUrl() + "/useraaa/user_info";
        };
    }

    @Override
    public Map<String, String> getData() throws NautaAttributeException {
        switch (portal) {
            case CONNECT -> {
                if (csrfHw == null) {
                    throw new NautaAttributeException("csrfHw is required");
                } else if (wlanUserIp == null) {
                    throw new NautaAttributeException("wlanUserIp is required");
                } else if (username == null) {
                    throw new NautaAttributeException("username is required");
                } else {
                    if (attributeUUID != null) {
                        return Map.of(
                                "op", "getLeftTime",
                                "ATTRIBUTE_UUID", attributeUUID,
                                "CSRFHW", csrfHw,
                                "wlanuserip", wlanUserIp,
                                "username", username
                        );
                    } else {
                        if (password == null) {
                            throw new NautaAttributeException("password is required");
                        } else {
                            return Map.of(
                                    "username", username,
                                    "password", password,
                                    "wlanuserip", wlanUserIp,
                                    "CSRFHW", csrfHw,
                                    "lang", ""
                            );
                        }
                    }
                }
            }
            case USER -> {
                return null;
            }
            default -> throw new IllegalArgumentException("Unsupported PortalManager: " + portal);
        }
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
