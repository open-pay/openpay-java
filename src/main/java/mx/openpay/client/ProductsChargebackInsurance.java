package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductsChargebackInsurance {


    @SerializedName("id")
    private String productId;

    @SerializedName("name")
    private String productName;
    
    @SerializedName("price")
    private BigDecimal productPrice;

    @SerializedName("quantity")
    private Integer productQuantity;

    private Category category;


}