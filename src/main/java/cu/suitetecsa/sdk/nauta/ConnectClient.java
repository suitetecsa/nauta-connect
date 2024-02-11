package cu.suitetecsa.sdk.nauta;

import cu.suitetecsa.sdk.nauta.exception.*;
import cu.suitetecsa.sdk.nauta.scraper.*;
import cu.suitetecsa.sdk.nauta.model.ConnectInformation;
import cu.suitetecsa.sdk.nauta.model.DataSession;
import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;
import cu.suitetecsa.sdk.nauta.network.PortalCommunicator;
import cu.suitetecsa.sdk.nauta.network.action.*;
import cu.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;
import cu.suitetecsa.sdk.nauta.utils.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;

import static cu.suitetecsa.sdk.nauta.utils.Constants.connectDomain;

public class ConnectClient {

    private static String @NotNull [] init(@NotNull PortalCommunicator communicator) throws NautaAttributeException, NautaException, LoadInfoException {
        FormParser scraper = new FormParser.Builder().build();

        SimpleEntry<String, Map<String, String>> loginResponse = communicator.performRequest(new GetPage("https://" + connectDomain + ":8443", null, HttpMethod.GET), scraper::parseLoginForm);
        String loginAction = loginResponse.getKey();
        Map<String, String> loginData = loginResponse.getValue();

        String wlanUserIp = loginData.getOrDefault("wlanuserip", "");
        String csrfHW = loginData.getOrDefault("CSRFHW", "");

        return new String[]{loginAction, wlanUserIp, csrfHW};
    }

    public static Boolean isConnected() throws NautaAttributeException, NautaException, LoadInfoException {
        PortalCommunicator communicator = new PortalCommunicator.Builder().build();
        ConnectionInfoParser scraper = new ConnectionInfoParser.Builder().build();
        return communicator.performRequest(new CheckConnection(null), httpResponse -> scraper.parseCheckConnection(httpResponse.getText()));
    }

    public static Long getRemainingTime(DataSession dataSession) throws NautaAttributeException, NautaException, LoadInfoException {
        if (!isConnected()) {
            throw new LoadInfoException("you are not connected. You need to be connected to get the remaining accountTime.");
        }

        PortalCommunicator communicator = new PortalCommunicator.Builder().build();

        Action action = new LoadUserInformation(dataSession.username(), null, dataSession.wlanUserIp(), dataSession.csrfHw(), dataSession.attributeUUID(), PortalManager.CONNECT);
        return communicator.performRequest(action, httpResponse -> {
            try {
                return StringUtils.toSeconds(httpResponse.getText());
            } catch (InvalidSessionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static ConnectInformation getUserInformation(String username, String password) throws NautaAttributeException, NautaException, LoadInfoException {
        PortalCommunicator communicator = new PortalCommunicator.Builder().build();
        ConnectionInfoParser scraper = new ConnectionInfoParser.Builder().build();

        String[] initResult = init(communicator);
        String wlanUserIp = initResult[1];
        String csrfHw = initResult[2];

        Action action = new LoadUserInformation(username, password, wlanUserIp, csrfHw, null, PortalManager.CONNECT);
        return communicator.performRequest(action, httpResponse -> {
            try {
                return scraper.parseConnectInformation(httpResponse);
            } catch (NautaException | LoadInfoException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Contract("_, _ -> new")
    public static @NotNull DataSession connect(String username, String password) throws NautaAttributeException, NautaException, LoadInfoException, LoginException {
        ExceptionHandler<LoginException> loginExceptionHandler = new ExceptionHandler.Builder<LoginException>().build();
        if (isConnected()) {
            throw loginExceptionHandler.handleException("Fail to connect", List.of("Already connected"));
        }

        PortalCommunicator communicator = new PortalCommunicator.Builder().build();
        ConnectionInfoParser scraper = new ConnectionInfoParser.Builder().build();

        String[] initResult = init(communicator);
        String wlanUserIp = initResult[1];
        String csrfHw = initResult[2];

        Action action = new Login(csrfHw, wlanUserIp, username, password, password, PortalManager.CONNECT, HttpMethod.POST);
        String uuid = communicator.performRequest(action, httpResponse -> {
            try {
                new ErrorParser.Builder().whenPortalManager(PortalManager.CONNECT).build().throwExceptionOnFailure(
                        httpResponse,
                        "Fail to login",
                        loginExceptionHandler
                );
                return scraper.parseAttributeUUID(httpResponse);
            } catch (LoadInfoException | LoginException | NotLoggedInException e) {
                throw new RuntimeException(e);
            }
        });
        return new DataSession(username, csrfHw, wlanUserIp, uuid);
    }

    public static void disconnect(DataSession dataSession) throws NautaAttributeException, NautaException, LoadInfoException, LogoutException {
        ExceptionHandler<LogoutException> logoutExceptionHandler = new ExceptionHandler.Builder<LogoutException>().build();
        if (!isConnected()) {
            throw logoutExceptionHandler.handleException("Fail to disconnect", List.of("You are not connected"));
        }

        PortalCommunicator communicator = new PortalCommunicator.Builder().build();
        ConnectionInfoParser scraper = new ConnectionInfoParser.Builder().build();

        Action action = new Logout(dataSession.username(), dataSession.wlanUserIp(), dataSession.csrfHw(), dataSession.attributeUUID());
        Boolean isLogout = communicator.performRequest(action, scraper::isSuccessLogout);
        if (!isLogout) throw logoutExceptionHandler.handleException("Failed to disconnect", List.of(""));
    }
}
