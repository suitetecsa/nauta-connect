package io.github.suitetecsa.sdk.nauta.model;

public record RechargesSummary(int count, String yearMonthSelected, double totalImport) implements Summary {
}
