package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class Category {

    @SerializedName("id")
    private BigInteger categoryId;

    @SerializedName("name")
    private String categoryName;

}
