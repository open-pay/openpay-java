package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString

public class OpenCheckoutConfigurationResponse {

    private String id;
    private String name;
    private BigDecimal amount;
    private BigDecimal iva;
    private String currency;
    private String description;
    @SerializedName("redirect_url")
    private String redirectUrl;
    @SerializedName("open_checkout_configuration_link")
    private String openCheckoutConfigurationLink;
    @SerializedName("expiration_date")
    private String expirationDate;
    @SerializedName("creation_date")
    private String creationDate;
    @SerializedName("deletion_date")
    private String deletionDate;
    private String status;
    @SerializedName("sales_number")
    private int salesNumber;
}
