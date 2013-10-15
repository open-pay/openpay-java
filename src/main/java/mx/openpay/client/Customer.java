package mx.openpay.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class Customer {

    private String name;
    
    private String id;
    
    private String email;
    
    @SerializedName("last_name")
    private String lastName;
    
    @SerializedName("phone_number")
    private String phoneNumber;
    
    private Address address;
    
    private String status;
    
}
