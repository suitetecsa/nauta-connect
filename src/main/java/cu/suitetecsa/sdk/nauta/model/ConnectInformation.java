package cu.suitetecsa.sdk.nauta.model;

import java.util.List;

public record ConnectInformation(AccountInfo accountInfo, List<LastConnection> lastConnections) {
}
