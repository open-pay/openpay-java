package mx.openpay.client.exceptions;


public class ServiceUnavailable extends Exception {

    private static final long serialVersionUID = -7388627000694002585L;

    public ServiceUnavailable(String message) {
        super(message);
    }

    public ServiceUnavailable(Throwable cause) {
        super(cause);
    }

    public ServiceUnavailable(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceUnavailable(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
