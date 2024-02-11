package cu.suitetecsa.sdk.nauta;

import cu.suitetecsa.sdk.nauta.exception.*;
import cu.suitetecsa.sdk.nauta.network.Action;
import cu.suitetecsa.sdk.nauta.network.HttpMethod;
import cu.suitetecsa.sdk.nauta.network.PortalCommunicator;
import cu.suitetecsa.sdk.nauta.network.action.TopUpBalance;
import cu.suitetecsa.sdk.nauta.network.action.TransferFunds;
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser;
import cu.suitetecsa.sdk.nauta.scraper.TokenParser;
import cu.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccountBalanceHandler {
    private static AccountBalanceHandler instance;
    private PortalCommunicator communicator;
    private String csrf;

    ErrorParser parser = new ErrorParser.Builder().whenPortalManager(PortalManager.CONNECT).build();

    private AccountBalanceHandler() {}

    public static AccountBalanceHandler getInstance(String sessionId) {
        if (instance == null) instance = new AccountBalanceHandler();
        SessionImpl session = (SessionImpl) new Session.Builder().build();
        session.cookies.put("session", sessionId);
        instance.communicator = new PortalCommunicator.Builder().withSession(session).build();
        return instance;
    }

    private String parseCsrfToken(@NotNull Action action) throws NautaException, LoadInfoException {
        TokenParser tokenParser = new TokenParser.Builder().build();
        return communicator.performRequest(action.getCsrfUrl() != null ? action.getCsrfUrl() : action.getUrl(), tokenParser::parseCsrfToken);
    }

    /**
     * Top up the balance with the given recharge code.
     * @param rechargeCode The recharge code to use.
     */
    public void topUpBalance(String rechargeCode) throws TopUpBalanceException, NautaException, LoadInfoException, NautaAttributeException {
        TopUpBalance action = new TopUpBalance(null, rechargeCode, HttpMethod.GET);
        ExceptionHandler<TopUpBalanceException> topUpExceptionHandle = new ExceptionHandler<>(TopUpBalanceException::new);

        csrf = parseCsrfToken(action);
        communicator.performRequest(action.copyWithCsrfAndMethod(csrf, HttpMethod.POST), httpResponse -> {
            try {
                parser.throwExceptionOnFailure(httpResponse, "Fail to top up balance", topUpExceptionHandle);
            } catch (TopUpBalanceException | NotLoggedInException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public void transferFunds(float amount, String password, String destinationAccount) throws TransferFundsException, NautaException, LoadInfoException, NautaAttributeException {
        TransferFunds action = new TransferFunds(null, amount, password, destinationAccount, HttpMethod.GET);
        ExceptionHandler<TransferFundsException> transferFundsExceptionHandle = new ExceptionHandler<>(TransferFundsException::new);

        csrf = parseCsrfToken(action);
        communicator.performRequest(action.copyWithCsrfAndMethod(csrf, HttpMethod.POST), httpResponse -> {
            try {
                parser.throwExceptionOnFailure(httpResponse, "Fail to top up balance", transferFundsExceptionHandle);
            } catch (TransferFundsException | NotLoggedInException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public void transferFunds(float amount, String password) throws NautaException, NautaAttributeException, LoadInfoException, TransferFundsException {
        transferFunds(amount, password, null);
    }
}
