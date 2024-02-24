package io.github.suitetecsa.sdk.nauta.model

@JvmRecord
data class AccountInfo(
    val accessAreas: String,
    val accountStatus: String,
    val credit: String,
    val expirationDate: String
)
