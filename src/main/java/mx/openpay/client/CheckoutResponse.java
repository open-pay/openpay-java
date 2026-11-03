package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;


@Getter
@Setter
@ToString(callSuper = true)
public class CheckoutResponse extends BaseCheckout {

    @SerializedName("creation_date")
    private String creationDate;

    @SerializedName("expiration_date")
    private String expirationDate;
}
