package cu.suitetecsa.sdk.nauta.exception;

public class LogoutException extends Exception {
    public LogoutException() {
        super();
    }
    public LogoutException(String message) {
        super(message);
    }
    public LogoutException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
