package cu.suitetecsa.sdk.nauta.model;

import java.util.List;

public class ConnectInformation {
    AccountInfo accountInfo;
    List<LastConnection> lastConnections;

    public ConnectInformation(AccountInfo accountInfo, List<LastConnection> lastConnections) {
        this.accountInfo = accountInfo;
        this.lastConnections = lastConnections;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public List<LastConnection> getLastConnections() {
        return lastConnections;
    }
}
