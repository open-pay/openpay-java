package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LendingCallbacks {

    @SerializedName("on_success")
    private String onSuccess;

    @SerializedName("on_reject")
    private String onReject;

    @SerializedName("on_canceled")
    private String onCanceled;

    @SerializedName("on_failed")
    private String onFailed;
}
