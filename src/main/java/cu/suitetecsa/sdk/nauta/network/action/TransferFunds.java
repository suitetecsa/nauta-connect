package cu.suitetecsa.sdk.nauta.network.action;

import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.ActionType;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;

import java.util.HashMap;
import java.util.Map;

public class TransferFunds implements Action {
    private final String csrf;
    private final float amount;
    private final String password;
    private final String destinationAccount;
    private final HttpMethod method;

    public TransferFunds(String csrf, float amount, String password, String destinationAccount, HttpMethod method) {
        this.csrf = csrf;
        this.amount = amount;
        this.password = password;
        this.destinationAccount = destinationAccount;
        this.method = method;
    }

    @Override
    public String getUrl() {
        return PortalManager.USER.getBaseUrl() + (destinationAccount != null ? "/useraaa/transfer_balance" : "/useraaa/transfer_nautahogarpaid");
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("csrf", csrf != null || method == HttpMethod.POST ? csrf : "");
        data.put("transfer", String.format("%.2f", amount).replace(".", ","));
        data.put("password_User", password);
        data.put("action", "checkdata");
        if (destinationAccount != null) {
            data.put("id_cuenta", destinationAccount);
        }
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
