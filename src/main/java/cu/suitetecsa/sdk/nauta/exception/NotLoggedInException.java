package cu.suitetecsa.sdk.nauta.exception;

public class NotLoggedInException extends Exception {
    public NotLoggedInException() {
        super();
    }
    public NotLoggedInException(String message) {
        super(message);
    }
    public NotLoggedInException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
