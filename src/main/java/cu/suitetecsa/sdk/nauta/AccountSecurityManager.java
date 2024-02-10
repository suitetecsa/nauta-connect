package cu.suitetecsa.sdk.nauta;

import cu.suitetecsa.sdk.nauta.exception.*;
import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;
import cu.suitetecsa.sdk.nauta.network.PortalCommunicator;
import cu.suitetecsa.sdk.nauta.network.action.ChangePassword;
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser;
import cu.suitetecsa.sdk.nauta.scraper.TokenParser;
import cu.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PortalSecurityManager {
    private static PortalSecurityManager instance;
    private PortalCommunicator communicator;
    private String sessionId;

    private PortalSecurityManager() {}

    public static PortalSecurityManager getInstance(String sessionId) {
        if (instance == null) instance = new PortalSecurityManager();
        instance.sessionId = sessionId;
        return instance;
    }

    private String parseCsrfToken(@NotNull Action action) throws NautaException, LoadInfoException {
        TokenParser tokenParser = new TokenParser.Builder().build();
        return communicator.performRequest(action.getCsrfUrl() != null ? action.getCsrfUrl() : action.getUrl(), tokenParser::parseCsrfToken);
    }

    private void changeAccountPassword(String oldPassword, String newPassword, Boolean isEmailAccount) throws NautaChangePasswordException, NautaException, LoadInfoException, NautaAttributeException {
        if (communicator == null) {
            SessionImpl session = new SessionImpl();
            session.cookies.put("session", sessionId);
            communicator = new PortalCommunicator.Builder().withSession(session).build();
        }

        ErrorParser parser = new ErrorParser.Builder().whenPortalManager(PortalManager.USER).build();

        ChangePassword action = new ChangePassword(null, oldPassword, newPassword, isEmailAccount, HttpMethod.GET);
        ExceptionHandler<NautaChangePasswordException> changePasswordExceptionHandler = new ExceptionHandler<>(NautaChangePasswordException::new);
        if (sessionId == null) throw changePasswordExceptionHandler.handleException("you are not logged in", List.of());
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
