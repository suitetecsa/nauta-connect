package io.github.suitetecsa.sdk.nauta.network.action;

import io.github.suitetecsa.sdk.nauta.network.Action;
import io.github.suitetecsa.sdk.nauta.network.ActionType;
import io.github.suitetecsa.sdk.nauta.network.HttpMethod;
import io.github.suitetecsa.sdk.nauta.utils.PortalManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record GetActions(int count, String yearMonthSelected, int pagesCount, boolean reversed,
                         ActionType type) implements Action {

    @Contract(pure = true)
    @Override
    public @NotNull String getUrl() {
        String baseUrl = PortalManager.USER.getBaseUrl();
        String actionPath = switch (type) {
            case Connections -> "/useraaa/service_detail_list/";
            case Recharges -> "/useraaa/recharge_detail_list/";
            case Transfers -> "/useraaa/transfer_detail_list/";
            case FeesPaid -> "/useraaa/nautahogarpaid_detail_list/";
        };
        return baseUrl + actionPath;
    }

    @Contract(pure = true)
    @Override
    public @Nullable Map<String, String> getData() {
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

    @Contract(pure = true)
    @Override
    public @Nullable String getCsrfUrl() {
        return null;
    }

    @Override
    public int getLarge() {
        return 0;
    }
}
