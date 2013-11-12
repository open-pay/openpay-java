package mx.openpay.client.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class HttpError extends Exception {

    private static final long serialVersionUID = -7388627000694002585L;

    private String category;

    private String description;

    @SerializedName("http_code")
    private Integer httpCode;

    private String requestId;

    public HttpError() {
        super();
    }

    public HttpError(final String message) {
        super(message);
    }

    public HttpError(final Throwable cause) {
        super(cause);
    }

    public HttpError(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HttpError(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
