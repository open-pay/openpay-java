package mx.openpay.client.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class OpenpayServiceException extends Exception {

    private static final long serialVersionUID = -7388627000694002585L;

    private String category;

    private String description;

    @SerializedName("http_code")
    private Integer httpCode;

    @SerializedName("error_code")
    private Integer errorCode;

    @SerializedName("request_id")
    private String requestId;

    public OpenpayServiceException() {
        super();
    }

    public OpenpayServiceException(final String message) {
        super(message);
    }

    public OpenpayServiceException(final Throwable cause) {
        super(cause);
    }

    public OpenpayServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public OpenpayServiceException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
