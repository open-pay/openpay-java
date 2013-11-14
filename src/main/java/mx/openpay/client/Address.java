package mx.openpay.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class Address {

    @SerializedName("postal_code")
    private String postalCode;

    private String line1;

    private String line2;

    private String line3;

    private String city;

    private String state;

    @SerializedName("country_code")
    private String countryCode;

}
