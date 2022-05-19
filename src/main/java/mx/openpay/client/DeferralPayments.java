package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import mx.openpay.client.enums.PaymentType;

/**
 * The Class DeferralPayments.
 */
@Data
@AllArgsConstructor
public class DeferralPayments {

    /** The payments. */
    private Integer payments;

    @SerializedName("payments_type")
    private PaymentType paymentsType;

}
