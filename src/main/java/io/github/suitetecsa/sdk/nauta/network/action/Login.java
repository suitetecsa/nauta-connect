package io.github.suitetecsa.sdk.nauta.network.action;

import io.github.suitetecsa.sdk.nauta.exception.NautaAttributeException;
import io.github.suitetecsa.sdk.nauta.network.Action;
import io.github.suitetecsa.sdk.nauta.network.ActionType;
import io.github.suitetecsa.sdk.nauta.network.HttpMethod;
import io.github.suitetecsa.sdk.nauta.utils.PortalManager;

import java.util.Map;
import java.util.Objects;

public class Login implements Action {
    private final String csrf;
    private final String wlanUserIp;
    private final String username;
    private final String password;
    private final String captchaCode;
    private final PortalManager portal;
    private final HttpMethod method;

    public Login(String csrf, String wlanUserIp, String username, String password, String captchaCode, PortalManager portal, HttpMethod method) {
        this.csrf = csrf;
        this.wlanUserIp = wlanUserIp;
        this.username = username;
        this.password = password;
        this.captchaCode = captchaCode;
        this.portal = portal;
        this.method = method;
    }

    public Login copyWithCsrfAndMethod(String csrf, HttpMethod method) {
        return new Login(csrf, this.wlanUserIp, this.username, this.password, this.captchaCode, this.portal, method);
    }

    @Override
    public String getUrl() {
        return switch (portal) {
            case CONNECT -> PortalManager.CONNECT.getBaseUrl() + "//LoginServlet";
            case USER -> "https://www.portal.nauta.cu/user/login/es-es";
        };
    }

    @Override
    public Map<String, String> getData() throws NautaAttributeException {
        switch (portal) {
            case CONNECT -> {
                if (csrf == null) {
                    throw new NautaAttributeException("CSRFHW is required");
                } else if (wlanUserIp == null) {
                    throw new NautaAttributeException("wlanuserip is required");
                }
                return Map.of(
                        "CSRFHW", csrf,
                        "wlanuserip", wlanUserIp,
                        "username", username,
                        "password", password
                );
            }
            case USER -> {
                return Map.of(
                        "csrf", csrf != null || method == HttpMethod.POST ? Objects.requireNonNull(csrf) : "",
                        "login_user", username,
                        "password_user", password,
                        "captcha", captchaCode,
                        "btn_submit", ""
                );
            }
            default -> throw new IllegalArgumentException("Unsupported PortalManager: " + portal);
        }
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
