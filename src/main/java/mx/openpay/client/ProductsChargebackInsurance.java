package mx.openpay.client;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import mx.openpay.commons.json.BigDecimalJsonSerializer;
import mx.openpay.commons.json.DefaultJsonSerialization;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductsChargebackInsurance {


    @SerializedName("id")
    private String productId;

    @SerializedName("name")
    private String productName;

    @JsonSerialize(using = BigDecimalJsonSerializer.class)
    @SerializedName("price")
    private BigDecimal productPrice;

    @SerializedName("quantity")
    private Integer productQuantity;

    private Category category;


}