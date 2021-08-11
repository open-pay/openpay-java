package mx.openpay.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LendingAddress {

    private String address;

    private String interior;

    private String neighborhood;

    private String state;

    private String city;

    private String zipcode;

    private String country;
}
