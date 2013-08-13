package mx.openpay.client;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class Address {

    private String city;

    private String region;

    @SerializedName("postal_code")
    private String postalCode;

    private String street;

    @SerializedName("interior_number")
    private String interiorNumber;

    @SerializedName("exterior_number")
    private String exteriorNumber;
}
