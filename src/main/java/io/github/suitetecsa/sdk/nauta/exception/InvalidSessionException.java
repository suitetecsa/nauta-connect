package io.github.suitetecsa.sdk.nauta.exception;

public class InvalidSessionException extends Exception {
    public InvalidSessionException() {
        super();
    }
    public InvalidSessionException(String message) {
        super(message);
    }
    public InvalidSessionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
