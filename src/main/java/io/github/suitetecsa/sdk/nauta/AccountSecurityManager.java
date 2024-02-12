package io.github.suitetecsa.sdk.nauta;

import io.github.suitetecsa.sdk.nauta.exception.*;
import io.github.suitetecsa.sdk.nauta.network.Action;
import io.github.suitetecsa.sdk.nauta.network.HttpMethod;
import io.github.suitetecsa.sdk.nauta.network.PortalCommunicator;
import io.github.suitetecsa.sdk.nauta.network.action.ChangePassword;
import io.github.suitetecsa.sdk.nauta.scraper.ErrorParser;
import io.github.suitetecsa.sdk.nauta.scraper.TokenParser;
import io.github.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import io.github.suitetecsa.sdk.nauta.utils.PortalManager;
import org.jetbrains.annotations.NotNull;

public class AccountSecurityManager {
    private static AccountSecurityManager instance;
    private PortalCommunicator communicator;

    private AccountSecurityManager() {}

    public static AccountSecurityManager getInstance(String sessionId) {
        if (instance == null) {
            instance = new AccountSecurityManager();
            SessionImpl session = (SessionImpl) new Session.Builder().build();
            session.cookies.put("session", sessionId);
            instance.communicator = new PortalCommunicator.Builder().withSession(session).build();
        }
        return instance;
    }

    private String parseCsrfToken(@NotNull Action action) throws NautaException, LoadInfoException {
        TokenParser tokenParser = new TokenParser.Builder().build();
        return communicator.performRequest(action.getCsrfUrl() != null ? action.getCsrfUrl() : action.getUrl(), tokenParser::parseCsrfToken);
    }

    private void changeAccountPassword(String oldPassword, String newPassword, Boolean isEmailAccount) throws NautaChangePasswordException, NautaException, LoadInfoException, NautaAttributeException {
        ErrorParser parser = new ErrorParser.Builder().whenPortalManager(PortalManager.USER).build();

        ChangePassword action = new ChangePassword(null, oldPassword, newPassword, isEmailAccount, HttpMethod.GET);
        ExceptionHandler<NautaChangePasswordException> changePasswordExceptionHandler = new ExceptionHandler<>(NautaChangePasswordException::new);
        String csrf = parseCsrfToken(action);
        communicator.performRequest(action.copyWithCsrfAndMethod(csrf, HttpMethod.POST), httpResponse -> {
            try {
                parser.throwExceptionOnFailure(httpResponse, "Fail to change password", changePasswordExceptionHandler);
            } catch (NautaChangePasswordException | NotLoggedInException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    /**
     * Changes the user's password.
     * @param oldPassword The user's current password.
     * @param newPassword The user's new password.
     */

    public void changePassword(String oldPassword, String newPassword) throws NautaChangePasswordException, NautaException, LoadInfoException, NautaAttributeException {
        changeAccountPassword(oldPassword, newPassword, false);
    }

    /**
     * Changes the user's email password.
     * @param oldPassword The user's current password.
     * @param newPassword The user's new password.
     */
    public void changeEmailPassword(String oldPassword, String newPassword) throws NautaException, NautaAttributeException, LoadInfoException, NautaChangePasswordException {
        changeAccountPassword(oldPassword, newPassword, true);
    }
}
