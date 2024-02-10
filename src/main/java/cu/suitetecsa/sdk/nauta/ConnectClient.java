package cu.suitetecsa.sdk.nauta;

import cu.suitetecsa.sdk.nauta.exception.*;
import cu.suitetecsa.sdk.nauta.model.ConnectInformation;
import cu.suitetecsa.sdk.nauta.model.DataSession;
import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;
import cu.suitetecsa.sdk.nauta.network.PortalCommunicator;
import cu.suitetecsa.sdk.nauta.network.action.*;
import cu.suitetecsa.sdk.nauta.scraper.ConnectionInfoParser;
import cu.suitetecsa.sdk.nauta.scraper.FormParser;
import cu.suitetecsa.sdk.nauta.scraper.JsoupFormParser;
import cu.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;
import cu.suitetecsa.sdk.nauta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;

import static cu.suitetecsa.sdk.nauta.utils.Constants.connectDomain;

public class ConnectClient {

    private static String @NotNull [] init() throws NautaAttributeException, NautaException, LoadInfoException {
        PortalCommunicator communicator = new JsoupPortalCommunicator.Builder().build();
        FormParser scraper = new JsoupFormParser();

        SimpleEntry<String, Map<String, String>> loginResponse = communicator.performRequest(new GetPage("https://" + connectDomain + ":8443", null, HttpMethod.GET), httpResponse -> scraper.parseLoginForm(httpResponse.getText()));
        String loginAction = loginResponse.getKey();
        Map<String, String> loginData = loginResponse.getValue();

        String wlanUserIp = loginData.getOrDefault("wlanuserip", "");
        String csrfHW = loginData.getOrDefault("CSRFHW", "");

        return new String[]{loginAction, wlanUserIp, csrfHW};
    }

    public static Boolean isConnected() throws NautaAttributeException, NautaException, LoadInfoException {
        PortalCommunicator communicator = new JsoupPortalCommunicator.Builder().build();
        ConnectionInfoParser scraper = new JsoupConnectionInfoParser();
        return communicator.performRequest(new CheckConnection(null), httpResponse -> scraper.parseCheckConnection(httpResponse.getText()));
    }

    public static Long getRemainingTime(DataSession dataSession) throws NautaAttributeException, NautaException, LoadInfoException {
        if (!isConnected()) {
            throw new LoadInfoException(
                    "you are not connected. You need to be connected to get the remaining time."
            );
        }

        PortalCommunicator communicator = new JsoupPortalCommunicator.Builder().build();

        Action action = new LoadUserInformation(dataSession.getUsername(), null, dataSession.getWlanUserIp(), dataSession.getCsrfHw(), dataSession.getAttributeUUID(), PortalManager.CONNECT);
        return communicator.performRequest(action, httpResponse -> {
            try {
                return StringUtils.toSeconds(httpResponse.getText());
            } catch (InvalidSessionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static ConnectInformation getUserInformation(String username, String password) throws NautaAttributeException, NautaException, LoadInfoException {
        String[] initResult = init();
        String wlanUserIp = initResult[1];
        String csrfHw = initResult[2];

        PortalCommunicator communicator = new JsoupPortalCommunicator.Builder().build();
        ConnectionInfoParser scraper = new JsoupConnectionInfoParser();

        Action action = new Login(csrfHw, wlanUserIp, username, password, null, PortalManager.CONNECT, HttpMethod.POST);
        return communicator.performRequest(action, httpResponse -> {
            try {
                return scraper.parseConnectInformation(httpResponse.getText());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static DataSession connect(String username, String password) throws NautaAttributeException, NautaException, LoadInfoException, LoginException {
        if (isConnected()) {
            ExceptionHandler<LoginException> loginExceptionHandler = new ExceptionHandler.Builder<LoginException>().build();
            throw loginExceptionHandler.handleException("Fail to connect", List.of("Already connected"));
        }

        String[] initResult = init();
        String wlanUserIp = initResult[1];
        String csrfHw = initResult[2];

        PortalCommunicator communicator = new JsoupPortalCommunicator.Builder().build();
        ConnectionInfoParser scraper = new JsoupConnectionInfoParser();

        Action action = new Login(csrfHw, wlanUserIp, username, password, password, PortalManager.CONNECT, HttpMethod.POST);
        return communicator.performRequest(action, httpResponse -> {
            try {
                return new DataSession(username, csrfHw, wlanUserIp, scraper.parseAttributeUUID(httpResponse.getText()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void disconnect(DataSession dataSession) throws NautaAttributeException, NautaException, LoadInfoException, LogoutException {
        ExceptionHandler<LogoutException> logoutExceptionHandler = new ExceptionHandler.Builder<LogoutException>().build();
        if (!isConnected()) {
            throw logoutExceptionHandler.handleException("Fail to disconnect", List.of("You are not connected"));
        }

        PortalCommunicator communicator = new JsoupPortalCommunicator.Builder().build();
        ConnectionInfoParser scraper = new JsoupConnectionInfoParser();

        Action action = new Logout(dataSession.getUsername(), dataSession.getWlanUserIp(), dataSession.getCsrfHw(), dataSession.getAttributeUUID());
        Boolean isLogout = communicator.performRequest(action, httpResponse -> scraper.isSuccessLogout(httpResponse.getText()));
        if (!isLogout) throw logoutExceptionHandler.handleException("Failed to disconnect", List.of(""));
    }
}
