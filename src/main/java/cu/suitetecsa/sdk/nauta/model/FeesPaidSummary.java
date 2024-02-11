package cu.suitetecsa.sdk.nauta.model;

public record FeesPaidSummary(int count, String yearMonthSelected, double totalImport) implements Summary {
}
