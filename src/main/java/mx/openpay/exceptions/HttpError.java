package mx.openpay.exceptions;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    public HttpError(String message) {
        super(message);
    }

    public HttpError(Throwable cause) {
        super(cause);
    }

    public HttpError(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
