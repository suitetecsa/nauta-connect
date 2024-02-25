package io.github.suitetecsa.sdk.nauta.model

@JvmRecord
data class DataSession(val username: String, val csrfHw: String, val wlanUserIp: String, val attributeUUID: String)
