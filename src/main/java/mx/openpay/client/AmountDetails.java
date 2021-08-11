package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class AmountDetails {

    private BigDecimal subtotal;

    private BigDecimal shipping;

    @SerializedName("handling_fee")
    private BigDecimal handlingFee;

    private BigDecimal tax;

    private BigDecimal discount;

}
