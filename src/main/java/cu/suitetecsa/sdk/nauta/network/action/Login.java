package cu.suitetecsa.sdk.nauta.network.action;

import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException;
import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.ActionType;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;

import java.util.HashMap;
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

    @Override
    public String getUrl() {
        return switch (portal) {
            case CONNECT -> PortalManager.CONNECT.getBaseUrl() + "//LoginServlet";
            case USER -> "/user/login/es-es";
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
