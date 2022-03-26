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
public class Checkout {

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

    @SerializedName("expiration_date")
    private Date expirationDate;

    @SerializedName("plan_id")
    private Integer planId;

    private Customer customer;

    private Transaction transaction;
}
