package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseCheckout {

    private String id;
    private BigDecimal amount;
    private String description;

    @SerializedName("order_id")
    private String orderId;

    private String currency;
    private String iva;
    private Taxes taxes;
    private Boolean tip;
    private String status;

    @SerializedName("checkout_link")
    private String checkoutLink;

    @SerializedName("plan_id")
    private Integer planId;

    private Customer customer;
    private Transaction transaction;

}