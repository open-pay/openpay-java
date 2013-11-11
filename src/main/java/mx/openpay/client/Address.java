package mx.openpay.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class Address {

    private String region;

    @SerializedName("postal_code")
    private String postalCode;

    private String street;

    private String state;

    @SerializedName("interior_number")
    private String interiorNumber;

    private String city;

    @SerializedName("exterior_number")
    private String exteriorNumber;
}
