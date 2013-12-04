package mx.openpay.client;

import lombok.Getter;
import lombok.Setter;

import com.google.gson.annotations.SerializedName;

/**
 * @author elopez
 */
@Getter
@Setter
public class Charge extends Transaction {

    private Refund refund;

    @SerializedName("payment_method")
    private PaymentMethod paymentMethod;

}
