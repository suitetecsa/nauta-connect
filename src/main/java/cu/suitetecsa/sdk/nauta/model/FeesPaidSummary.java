package cu.suitetecsa.sdk.nauta.model;

public record FeePaysSummary(int count, String yearMonthSelected, double totalImport) implements Summary {
}
