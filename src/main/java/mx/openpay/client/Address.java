package mx.openpay.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an Address object.
 */
@Getter
@Setter
@ToString
public class Address {

    /** Postal code. Required. */
    @SerializedName("postal_code")
    private String postalCode;

    /** First line of address. Required. */
    private String line1;

    /** Second line of address. Optional. */
    private String line2;

    /** Third line of address. Optional. */
    private String line3;

    /** City. Required. */
    private String city;

    /** State. Required. */
    private String state;

    /** Two-letter ISO 3166-1 country code. Optional. */
    @SerializedName("country_code")
    private String countryCode;

}
