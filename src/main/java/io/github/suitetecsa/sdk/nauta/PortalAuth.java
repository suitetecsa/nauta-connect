package io.github.suitetecsa.sdk.nauta;

import io.github.suitetecsa.sdk.nauta.exception.*;
import io.github.suitetecsa.sdk.nauta.model.AccountDetail;
import io.github.suitetecsa.sdk.nauta.network.Action;
import io.github.suitetecsa.sdk.nauta.network.HttpMethod;
import io.github.suitetecsa.sdk.nauta.network.PortalCommunicator;
import io.github.suitetecsa.sdk.nauta.network.action.GetCaptcha;
import io.github.suitetecsa.sdk.nauta.network.action.LoadUserInformation;
import io.github.suitetecsa.sdk.nauta.network.action.Login;
import io.github.suitetecsa.sdk.nauta.scraper.AccountParser;
import io.github.suitetecsa.sdk.nauta.scraper.TokenParser;
import io.github.suitetecsa.sdk.nauta.utils.PortalManager;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;

public class PortalAuth {

    public static PortalAuth instance;
    private PortalCommunicator communicator;
    private AccountParser parser;
    private String sessionId;
    private String csrf;

    private String parseCsrfToken(@NotNull Action action) throws NautaException, LoadInfoException {
        TokenParser tokenParser = new TokenParser.Builder().build();
        return communicator.performRequest(action.getCsrfUrl() != null ? action.getCsrfUrl() : action.getUrl(), tokenParser::parseCsrfToken);
    }

    public static PortalAuth getInstance(String sessionId) {
        if (instance == null) {
            instance = new PortalAuth();
            if (sessionId != null) {
                SessionImpl session = (SessionImpl) new Session.Builder().build();
                session.cookies.put("session", sessionId);
                instance.communicator = new PortalCommunicator.Builder().withSession(session).build();
            } else if (instance.communicator == null) {
                instance.communicator = new PortalCommunicator.Builder().build();
            }
        }
        return instance;
    }

    /**
     * Load the captcha image for the user portal authentication API
     * @return the captcha image
     */
    public byte[] getCaptchaImage() throws NautaAttributeException, NautaException, LoadInfoException {
        return communicator.performRequest(new GetCaptcha(), httpResponse -> {
            sessionId = httpResponse.cookies().getOrDefault("session", null);
            return httpResponse.content();
        });
    }

    /**
     * Load the user information for the user portal authentication API
     * @return the user information
     * @throws NotLoggedInException if the user is not logged in
     */
    public AccountDetail getAccountDetails() throws NotLoggedInException, NautaAttributeException, NautaException, LoadInfoException {
        if (sessionId == null) throw new NotLoggedInException("Failed to get user information :: You are not logged in");
        if (parser == null) parser = new AccountParser.Builder().build();
        Action action = new LoadUserInformation(null, null, null, null, null, PortalManager.USER);
        return communicator.performRequest(action, httpResponse -> {
            try {
                return parser.parseAccount(httpResponse);
            } catch (NautaGetInfoException | NotLoggedInException | ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Login to the user portal authentication API
     * @param username the username of the account
     * @param password the password of the account
     * @param captchaCode the captcha code
     * @return the user information
     */
    public AccountDetail login(String username, String password, String captchaCode) throws NautaAttributeException, NautaException, LoadInfoException {
        if (parser == null) parser = new AccountParser.Builder().build();
        Login action = new Login(null, null, username, password, captchaCode, PortalManager.USER, HttpMethod.GET);
        if (csrf == null) csrf = parseCsrfToken(action);
        return communicator.performRequest(action.copyWithCsrfAndMethod(csrf, HttpMethod.POST), httpResponse -> {
            try {
                return parser.parseAccount(httpResponse);
            } catch (NautaGetInfoException | NotLoggedInException | ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
