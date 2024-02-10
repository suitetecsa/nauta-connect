package cu.suitetecsa.sdk.nauta.exception;

public class NautaException extends Exception {
    public NautaException() {
        super();
    }
    public NautaException(String message) {
        super(message);
    }
    public NautaException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
