package io.github.suitetecsa.sdk.nauta.model;

import java.util.List;

public record ConnectInformation(AccountInfo accountInfo, List<LastConnection> lastConnections) {
}
