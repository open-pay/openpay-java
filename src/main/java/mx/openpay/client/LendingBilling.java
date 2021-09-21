package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LendingBilling {

    private String name;
    
    private String lastName;

    private String rfc;

    @SerializedName("phone_number")
    private String phoneNumber;

    private String email;

    private LendingAddress address;
}
