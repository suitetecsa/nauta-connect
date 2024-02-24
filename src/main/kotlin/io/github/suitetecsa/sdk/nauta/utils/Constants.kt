package io.github.suitetecsa.sdk.nauta.utils

object Constants {
    const val CONNECT_DOMAIN: String = "secure.etecsa.net"
    const val VARIOUS_ERRORS_DETECTED: String = "Se han detectado algunos errores."
    @JvmField
    val notLoggedInErrors: List<String> = listOf("Usted no tiene permisos para acceder a la operación.")
}
