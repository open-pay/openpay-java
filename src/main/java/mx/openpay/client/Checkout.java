package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true)
public class Checkout extends BaseCheckout {

    @SerializedName("expiration_date")
    private Date expirationDate;

    @SerializedName("origin")
    private String originChannel;
}