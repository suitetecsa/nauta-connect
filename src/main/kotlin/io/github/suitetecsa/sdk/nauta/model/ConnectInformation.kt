package io.github.suitetecsa.sdk.nauta.model

@JvmRecord
data class ConnectInformation(val accountInfo: AccountInfo, val lastConnections: List<LastConnection>)
