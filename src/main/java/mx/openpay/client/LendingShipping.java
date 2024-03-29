package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mx.openpay.client.enums.LendingShippingType;

@Getter
@Setter
@ToString
public class LendingShipping {

    private String name;

    @SerializedName("last_name")
    private String lastName;

    private LendingAddress address;

    @SerializedName("phone_number")
    private String phoneNumber;

    private String email;

    private String type;
}
