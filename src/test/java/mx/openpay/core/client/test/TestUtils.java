package mx.openpay.core.client.test;

import mx.openpay.client.Address;

/**
 * @author elopez
 */
public class TestUtils {

    public static Address prepareAddress() {
        Address address = new Address();
        address.setCity("Querétaro");
        address.setLine1("Camino #11 - 01");
        address.setPostalCode("76090");
        address.setState("Queretaro");
        address.setCountryCode("MX");
        return address;
    }

}
