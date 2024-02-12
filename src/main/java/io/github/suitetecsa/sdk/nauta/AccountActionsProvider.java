package io.github.suitetecsa.sdk.nauta;

import io.github.suitetecsa.sdk.nauta.exception.*;
import io.github.suitetecsa.sdk.nauta.model.*;
import io.github.suitetecsa.sdk.nauta.network.*;
import io.github.suitetecsa.sdk.nauta.network.action.GetActions;
import io.github.suitetecsa.sdk.nauta.network.action.GetSummary;
import io.github.suitetecsa.sdk.nauta.scraper.ActionsParser;
import io.github.suitetecsa.sdk.nauta.scraper.ActionsSummaryParser;
import io.github.suitetecsa.sdk.nauta.scraper.ErrorParser;
import io.github.suitetecsa.sdk.nauta.scraper.TokenParser;
import io.github.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import io.github.suitetecsa.sdk.nauta.utils.SummaryUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class AccountActionsProvider {
    private static AccountActionsProvider instance;
    private PortalCommunicator communicator;
    private String csrf;

    private final ExceptionHandler<LoadInfoException> loadInfoExceptionHandler = new ExceptionHandler<>(LoadInfoException::new);
    private final ErrorParser errorParser = new ErrorParser.Builder().build();
    private final ActionsSummaryParser summaryParser = new ActionsSummaryParser.Builder().build();
    private final ActionsParser actionsParser = new ActionsParser.Builder().build();

    private AccountActionsProvider() {}

    public static AccountActionsProvider getInstance(String sessionId) {
        if (instance == null) {
            instance = new AccountActionsProvider();
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

    private <T> List<T> getActions(Action action, int pageTo, Function<HttpResponse, List<T>> transform) throws NautaException, LoadInfoException {
        if (pageTo != 0) {
            return communicator.performRequest(action.getUrl() + action.yearMonthSelected() + "/" + action.count() + "/" + pageTo, transform);
        } else {
            List<T> list = new ArrayList<>();
            for (int page = 0; page < action.pagesCount(); page++) {
                String url = action.getUrl() + action.yearMonthSelected() + "/" +
                        action.count() + (page > 0 ? "/" + (page + 1) : "");
                communicator.performRequest(url, httpResponse -> list.addAll(transform.apply(httpResponse)));
            }
            return list;
        }
    }

    ConnectionsSummary getConnectionsSummary(int year, int month) throws NautaException, LoadInfoException, NautaAttributeException {
        GetSummary action = new GetSummary(null, year, month, ActionType.Connections, HttpMethod.GET);
        csrf = parseCsrfToken(action);
        return communicator.performRequest(action.copyWithCsrfAndMethod(csrf, HttpMethod.POST), httpResponse -> {
            try {
                return summaryParser.parseConnectionsSummary(errorParser.throwExceptionOnFailure(httpResponse, "Fail to load connections summary", loadInfoExceptionHandler));
            } catch (LoadInfoException | NotLoggedInException | InvalidSessionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    RechargesSummary getRechargesSummary(int year, int month) throws NautaException, LoadInfoException, NautaAttributeException {
        GetSummary action = new GetSummary(null, year, month, ActionType.Recharges, HttpMethod.GET);
        csrf = parseCsrfToken(action);
        return communicator.performRequest(action.copyWithCsrfAndMethod(csrf, HttpMethod.POST), httpResponse -> {
            try {
                return summaryParser.parseRechargesSummary(errorParser.throwExceptionOnFailure(httpResponse, "Fail to load recharges summary", loadInfoExceptionHandler));
            } catch (LoadInfoException | NotLoggedInException e) {
                throw new RuntimeException(e);
            }
        });
    }

    TransfersSummary getTransfersSummary(int year, int month) throws NautaException, LoadInfoException, NautaAttributeException {
        GetSummary action = new GetSummary(null, year, month, ActionType.Transfers, HttpMethod.GET);
        csrf = parseCsrfToken(action);
        return communicator.performRequest(action.copyWithCsrfAndMethod(csrf, HttpMethod.POST), httpResponse -> {
            try {
                return summaryParser.parseTransfersSummary(errorParser.throwExceptionOnFailure(httpResponse, "Fail to load transfers summary", loadInfoExceptionHandler));
            } catch (LoadInfoException | NotLoggedInException e) {
                throw new RuntimeException(e);
            }
        });
    }

    FeesPaidSummary getFeesPaidSummary(int year, int month) throws NautaException, LoadInfoException, NautaAttributeException {
        GetSummary action = new GetSummary(null, year, month, ActionType.FeesPaid, HttpMethod.GET);
        csrf = parseCsrfToken(action);
        return communicator.performRequest(action.copyWithCsrfAndMethod(csrf, HttpMethod.POST), httpResponse -> {
            try {
                return summaryParser.parseFeesPaidSummary(errorParser.throwExceptionOnFailure(httpResponse, "Fail to load fees paid summary", loadInfoExceptionHandler));
            } catch (LoadInfoException | NotLoggedInException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<Connection> getConnections(@NotNull ConnectionsSummary summary, int pageTo, boolean reversed) throws NautaAttributeException, NautaException, LoadInfoException {
        GetActions action = new GetActions(
                summary.count(),
                summary.yearMonthSelected(),
                SummaryUtils.getPagesCount(summary),
                reversed,
                ActionType.Connections
        );
        if (pageTo > SummaryUtils.getPagesCount(summary))
            throw new NautaAttributeException("Page to is greater than pages count");
        if (summary.count() != 0) {
            return getActions(action, pageTo, httpResponse -> {
                try {
                    return actionsParser.parseConnections(errorParser.throwExceptionOnFailure(httpResponse, "Fail to load connections", loadInfoExceptionHandler));
                } catch (NautaException | LoadInfoException | NotLoggedInException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            return Collections.emptyList();
        }
    }

    List<Connection> getConnections(int year, int month, int pageTo, boolean reversed) throws NautaException, NautaAttributeException, LoadInfoException {
        return getConnections(getConnectionsSummary(year, month), pageTo, reversed);
    }

    public List<Recharge> getRecharges(@NotNull RechargesSummary summary, int pageTo, boolean reversed) throws NautaAttributeException, NautaException, LoadInfoException {
        GetActions action = new GetActions(
                summary.count(),
                summary.yearMonthSelected(),
                SummaryUtils.getPagesCount(summary),
                reversed,
                ActionType.Recharges
        );
        if (pageTo > SummaryUtils.getPagesCount(summary))
            throw new NautaAttributeException("Page to is greater than pages count");
        if (summary.count() != 0) {
            return getActions(action, pageTo, httpResponse -> {
                try {
                    return actionsParser.parseRecharges(errorParser.throwExceptionOnFailure(httpResponse, "Fail to load recharges", loadInfoExceptionHandler));
                } catch (NautaException | LoadInfoException | NotLoggedInException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            return Collections.emptyList();
        }
    }

    List<Recharge> getRecharges(int year, int month, int pageTo, boolean reversed) throws NautaException, NautaAttributeException, LoadInfoException {
        return getRecharges(getRechargesSummary(year, month), pageTo, reversed);
    }

    public List<Transfer> getTransfers(@NotNull TransfersSummary summary, int pageTo, boolean reversed) throws NautaAttributeException, NautaException, LoadInfoException {
        GetActions action = new GetActions(
                summary.count(),
                summary.yearMonthSelected(),
                SummaryUtils.getPagesCount(summary),
                reversed,
                ActionType.Transfers
        );
        if (pageTo > SummaryUtils.getPagesCount(summary))
            throw new NautaAttributeException("Page to is greater than pages count");
        if (summary.count() != 0) {
            return getActions(action, pageTo, httpResponse -> {
                try {
                    return actionsParser.parseTransfers(errorParser.throwExceptionOnFailure(httpResponse, "Fail to load transfers", loadInfoExceptionHandler));
                } catch (NautaException | LoadInfoException | NotLoggedInException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            return Collections.emptyList();
        }
    }

    List<Transfer> getTransfers(int year, int month, int pageTo, boolean reversed) throws NautaException, NautaAttributeException, LoadInfoException {
        return getTransfers(getTransfersSummary(year, month), pageTo, reversed);
    }

    public List<FeePaid> getFeesPaid(@NotNull FeesPaidSummary summary, int pageTo, boolean reversed) throws NautaAttributeException, NautaException, LoadInfoException {
        GetActions action = new GetActions(
                summary.count(),
                summary.yearMonthSelected(),
                SummaryUtils.getPagesCount(summary),
                reversed,
                ActionType.FeesPaid
        );
        if (pageTo > SummaryUtils.getPagesCount(summary))
            throw new NautaAttributeException("Page to is greater than pages count");
        if (summary.count() != 0) {
            return getActions(action, pageTo, httpResponse -> {
                try {
                    return actionsParser.parseFeesPaid(errorParser.throwExceptionOnFailure(httpResponse, "Fail to load fees paid", loadInfoExceptionHandler));
                } catch (NautaException | LoadInfoException | NotLoggedInException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            return Collections.emptyList();
        }
    }

    List<FeePaid> getFeesPaid(int year, int month, int pageTo, boolean reversed) throws NautaException, NautaAttributeException, LoadInfoException {
        return getFeesPaid(getFeesPaidSummary(year, month), pageTo, reversed);
    }
}
