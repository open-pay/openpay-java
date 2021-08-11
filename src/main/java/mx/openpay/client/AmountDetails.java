package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AmountDetails {

    private double subtotal;

    private double shipping;

    @SerializedName("handling_fee")
    private double handlingFee;

    private double tax;

    private double discount;

}
