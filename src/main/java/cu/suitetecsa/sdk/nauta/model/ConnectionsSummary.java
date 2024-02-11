package cu.suitetecsa.sdk.nauta.model;

public record ConnectionsSummary(
        int count,
        String yearMonthSelected,
        long totalTime,
        double totalImport,
        long uploaded,
        long downloaded,
        long totalTraffic
) implements Summary {
}
