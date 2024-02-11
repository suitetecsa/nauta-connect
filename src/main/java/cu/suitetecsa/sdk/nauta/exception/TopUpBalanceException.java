package cu.suitetecsa.sdk.nauta.exception;

public class TopUpBalanceException extends Exception {
    public TopUpBalanceException() {
        super();
    }
    public TopUpBalanceException(String message) {
        super(message);
    }
    public TopUpBalanceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
