package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;


@Getter
@Setter
@ToString(callSuper = true)
public class CheckoutResponse {


    private String id;

    private BigDecimal amount;

    private String description;

    @SerializedName("order_id")
    private String orderId;

    private String currency;

    private String iva;

    private String status;

    @SerializedName("checkout_link")
    private String checkoutLink;

    @SerializedName("creation_date")
    private String creationDate;

    @SerializedName("expiration_date")
    private String expirationDate;

    @SerializedName("plan_id")
    private Integer planId;

    private Customer customer;

    private Transaction transaction;
}

