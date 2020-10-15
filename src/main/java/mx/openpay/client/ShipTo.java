package mx.openpay.client;

import lombok.Getter;
import lombok.Setter;

import com.google.gson.annotations.SerializedName;

/**
 * Shipping information.
 */
@Getter
@Setter
public class ShipTo {

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("phone_number")
    private String phoneNumber;

    private String email;

    private String method;

    @SerializedName("phone_area_code")
    private String phoneAreaCode;

    private Address address;

    public ShipTo firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ShipTo lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ShipTo phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public ShipTo email(String email) {
        this.email = email;
        return this;
    }

    public ShipTo method(String method) {
        this.method = method;
        return this;
    }

    public ShipTo address(Address address) {
        this.address = address;
        return this;
    }

    public ShipTo phoneAreaCode(String phoneAreaCode) {
        this.phoneAreaCode = phoneAreaCode;
        return this;
    }

}
