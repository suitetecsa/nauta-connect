package io.github.suitetecsa.sdk.nauta.exception;

public class LoginException extends Exception {
    public LoginException() {
        super();
    }
    public LoginException(String message) {
        super(message);
    }
    public LoginException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
