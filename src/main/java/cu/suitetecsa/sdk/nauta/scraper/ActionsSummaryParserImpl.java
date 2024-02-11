package cu.suitetecsa.sdk.nauta.scraper;

import cu.suitetecsa.sdk.nauta.exception.InvalidSessionException;
import cu.suitetecsa.sdk.nauta.exception.LoadInfoException;
import cu.suitetecsa.sdk.nauta.exception.NotLoggedInException;
import cu.suitetecsa.sdk.nauta.model.*;
import cu.suitetecsa.sdk.nauta.network.HttpResponse;
import cu.suitetecsa.sdk.nauta.scraper.ActionsSummaryParser;
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser;
import cu.suitetecsa.sdk.nauta.utils.ExceptionHandler;
import cu.suitetecsa.sdk.nauta.utils.PortalManager;
import cu.suitetecsa.sdk.nauta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ActionsSummaryParserImpl implements ActionsSummaryParser {
    private final ExceptionHandler<LoadInfoException> loadInfoExceptionHandler = new ExceptionHandler<>(LoadInfoException::new);
    ErrorParser parser = new ErrorParser.Builder().whenPortalManager(PortalManager.USER).build();

    private <T extends Summary> T parseSummary(@NotNull HttpResponse httpResponse, @NotNull Transform<T> transform) throws LoadInfoException, NotLoggedInException {
        Document parsedHtml = Jsoup.parse(httpResponse.getText());
        parser.throwExceptionOnFailure(httpResponse, "Fail to get summary", loadInfoExceptionHandler);
        Elements contentCards = parsedHtml.selectFirst("#content").select(".card-content");
        return transform.apply(
                Integer.parseInt(contentCards.get(0).selectFirst("input[name=count]").attr("value")),
                contentCards.get(0).selectFirst("input[name=year_month_selected]").attr("value"),
                StringUtils.fromPriceString(contentCards.get(1).selectFirst(".card-stats-number").text().trim())
        );
    }

    @FunctionalInterface
    private interface Transform<T extends Summary> {
        T apply(int count, String yearMonthSelected, double totalImport) throws LoadInfoException;
    }

    @Override
    public ConnectionsSummary parseConnectionsSummary(@NotNull HttpResponse httpResponse) throws LoadInfoException, NotLoggedInException, InvalidSessionException {
        Document parsedHtml = Jsoup.parse(httpResponse.getText());
        parser.throwExceptionOnFailure(httpResponse, "Fail to get summary", loadInfoExceptionHandler);
        Elements contentCards = parsedHtml.selectFirst("#content").select(".card-content");
        return new ConnectionsSummary(
                Integer.parseInt(contentCards.get(0).selectFirst("input[name=count]").attr("value")),
                contentCards.get(0).selectFirst("input[name=year_month_selected]").attr("value"),
                StringUtils.toSeconds(contentCards.get(1).selectFirst(".card-stats-number").text().trim()),
                StringUtils.fromPriceString(contentCards.get(2).selectFirst(".card-stats-number").text().trim()),
                StringUtils.toBytes(contentCards.get(3).selectFirst(".card-stats-number").text().trim()),
                StringUtils.toBytes(contentCards.get(4).selectFirst(".card-stats-number").text().trim()),
                StringUtils.toBytes(contentCards.get(5).selectFirst(".card-stats-number").text().trim())
        );
    }

    @Override
    public RechargesSummary parseRechargesSummary(HttpResponse httpResponse) throws LoadInfoException, NotLoggedInException {
        return parseSummary(httpResponse, (RechargesSummary::new));
    }

    @Override
    public TransfersSummary parseTransfersSummary(HttpResponse httpResponse) throws LoadInfoException, NotLoggedInException {
        return parseSummary(httpResponse, (TransfersSummary::new));
    }

    @Override
    public FeesPaidSummary parseFeesPaidSummary(HttpResponse httpResponse) throws LoadInfoException, NotLoggedInException {
        return parseSummary(httpResponse, (FeesPaidSummary::new));
    }
}
