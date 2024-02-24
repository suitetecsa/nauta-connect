package io.github.suitetecsa.sdk.nauta

import io.github.suitetecsa.sdk.nauta.exception.*
import io.github.suitetecsa.sdk.nauta.model.ConnectInformation
import io.github.suitetecsa.sdk.nauta.model.DataSession
import io.github.suitetecsa.sdk.nauta.network.*
import io.github.suitetecsa.sdk.nauta.network.ConnectRoute.*
import io.github.suitetecsa.sdk.nauta.scraper.ConnectionInfoParser
import io.github.suitetecsa.sdk.nauta.scraper.ErrorParser
import io.github.suitetecsa.sdk.nauta.scraper.FormParser
import io.github.suitetecsa.sdk.nauta.utils.*
import org.jetbrains.annotations.Contract

object ConnectClient {
    @Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class)
    private fun init(communicator: PortalCommunicator): Array<String> {
        val scraper = FormParser.Builder().build()

        val loginResponse = communicator.performRequest(Init, scraper::parseLoginForm)
        val loginAction = loginResponse.key
        val loginData = loginResponse.value

        val wlanUserIp = loginData.getOrDefault("wlanuserip", "")
        val csrfHW = loginData.getOrDefault("CSRFHW", "")

        return arrayOf(loginAction, wlanUserIp, csrfHW)
    }

    /**
     * Verifica si hay conexión a internet
     *
     * @return true si hay conexión, false en caso contrario
     * @throws NautaAttributeException sí hay un error obteniendo los atributos
     * @throws NautaException sí hay un error de conexión general
     * @throws LoadInfoException sí hay un error cargando la información
     */
    @JvmStatic
    @get:Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class)
    val isConnected: Boolean
        get() {
            val communicator = PortalCommunicator.Builder().build()
            val scraper = ConnectionInfoParser.Builder().build()
            return communicator.performRequest(CheckAccess, scraper::parseCheckConnection)
        }

    /**
     * Obtiene el tiempo restante de la sesión
     *
     * @param dataSession datos de la sesión
     * @return el tiempo restante en segundos
     * @throws NautaAttributeException si hay error obteniendo los atributos
     * @throws NautaException si hay error de conexión
     * @throws LoadInfoException si no hay conexión
     */
    @JvmStatic
    @Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class)
    fun getRemainingTime(dataSession: DataSession): Long {
        if (!isConnected) {
            throw LoadInfoException("you are not connected. You need to be connected to get the remaining accountTime.")
        }

        val communicator = PortalCommunicator.Builder().build()

        val route: ConnectRoute = GetTime(
            username = dataSession.username,
            wlanUserIp = dataSession.wlanUserIp,
            csrfHw = dataSession.csrfHw,
            attributeUUID = dataSession.attributeUUID
        )
        return communicator.performRequest(route) { StringUtils.toSeconds(it.text) }
    }

    /**
     * Obtiene la información de conexión del usuario
     *
     * @param username el nombre de usuario
     * @param password la contraseña
     * @return la información de conexión
     * @throws NautaAttributeException si hay error obteniendo atributos
     * @throws NautaException si hay error de conexión
     * @throws LoadInfoException sí hay error cargando la información
     */
    @JvmStatic
    @Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class)
    fun getUserInformation(username: String, password: String): ConnectInformation {
        val communicator = PortalCommunicator.Builder().build()
        val scraper = ConnectionInfoParser.Builder().build()

        val initResult = init(communicator)
        val wlanUserIp = initResult[1]
        val csrfHw = initResult[2]

        val route: ConnectRoute = UserInfo(
            username = username,
            password = password,
            wlanUserIp = wlanUserIp,
            csrfHw = csrfHw
        )
        return communicator.performRequest(route, scraper::parseConnectInformation)
    }

    /**
     * Realiza la conexión a internet con las credenciales dadas
     *
     * @param username el nombre de usuario
     * @param password la contraseña
     * @return la sesión de datos de la conexión
     * @throws NautaAttributeException si hay error obteniendo atributos
     * @throws NautaException si hay error de conexión
     * @throws LoadInfoException sí hay error cargando información
     * @throws LoginException si falla el login
     */
    @JvmStatic
    @Contract("_, _ -> new")
    @Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class, LoginException::class)
    fun connect(username: String, password: String): DataSession {
        val loginExceptionHandler = ExceptionHandler { LoginException(it) }
        if (isConnected) {
            throw loginExceptionHandler.handleException("Fail to connect", listOf("Already connected"))
        }

        val communicator = PortalCommunicator.Builder().build()
        val scraper = ConnectionInfoParser.Builder().build()

        val initResult = init(communicator)
        val wlanUserIp = initResult[1]
        val csrfHw = initResult[2]

        val route: ConnectRoute =
            Connect(username = username, password = password, csrfHw = csrfHw, wlanUserIp = wlanUserIp)
        val uuid = communicator.performRequest(route) { httpResponse: HttpResponse ->
            ErrorParser.Builder().whenPortalManager(PortalManager.CONNECT).build().throwExceptionOnFailure(
                httpResponse,
                "Fail to login",
                loginExceptionHandler
            )
            scraper.parseAttributeUUID(httpResponse)
        }
        return DataSession(username, csrfHw, wlanUserIp, uuid)
    }

    /**
     * Desconecta la sesión actual
     *
     * @param dataSession datos de la sesión a desconectar
     * @throws NautaAttributeException si hay error obteniendo atributos
     * @throws NautaException si hay error de conexión
     * @throws LoadInfoException si no hay conexión
     * @throws LogoutException si falla el logout
     */
    @JvmStatic
    @Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class, LogoutException::class)
    fun disconnect(dataSession: DataSession) {
        val logoutExceptionHandler = ExceptionHandler { LogoutException(it) }
        if (!isConnected) {
            throw logoutExceptionHandler.handleException("Fail to disconnect", listOf("You are not connected"))
        }

        val communicator = PortalCommunicator.Builder().build()
        val scraper = ConnectionInfoParser.Builder().build()

        val route: ConnectRoute = Disconnect(
            username = dataSession.username,
            wlanUserIp = dataSession.wlanUserIp,
            csrfHw = dataSession.csrfHw,
            attributeUUID = dataSession.attributeUUID
        )
        val isLogout = communicator.performRequest(route, scraper::isSuccessLogout)
        if (!isLogout) throw logoutExceptionHandler.handleException("Failed to disconnect", listOf(""))
    }
}
