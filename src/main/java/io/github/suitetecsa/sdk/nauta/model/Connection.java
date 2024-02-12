package io.github.suitetecsa.sdk.nauta.model;

public record Connection(long startSession, long endSession, long duration, long uploaded, long downloaded, double import_) {
}
