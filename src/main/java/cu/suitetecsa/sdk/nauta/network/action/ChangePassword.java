package cu.suitetecsa.sdk.nauta.network.action;

import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.ActionType;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword implements Action {
    private final String csrf;
    private final String oldPassword;
    private final String newPassword;
    private final boolean changeMail;

    public ChangePassword(String csrf, String oldPassword, String newPassword, boolean changeMail) {
        this.csrf = csrf;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.changeMail = changeMail;
    }

    @Override
    public String getUrl() {
        return PortalManager.USER.getBaseUrl() + (changeMail ? "/mail/change_password" : "/Useraaa/change_password");
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("csrf", csrf != null ? csrf : "");
        data.put("old_password", oldPassword);
        data.put("new_password", newPassword);
        data.put("repeat_new_password", newPassword);
        data.put("btn_submit", "");
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
        return ActionType.Connections;
    }
}
