package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;

import java.util.List;

import junit.framework.Assert;
import mx.openpay.client.Address;
import mx.openpay.client.Customer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.Before;
import org.junit.Test;

public class CustomerOperationsTest {

    @Before
    public void setUp() throws Exception {
        OpenpayAPI.configure(ENDPOINT, API_KEY, MERCHANT_ID);
    }

    @Test
    public void testCreateAndDeleteCustomer() throws ServiceUnavailableException, OpenpayServiceException {
        Address address = this.createAddress();
        Customer customer = Customer.create("Juan", "Perez Perez", "juan.perez@gmail.com",
                "55-25634013", address);
        Customer.delete(customer.getId());
        Assert.assertNotNull(customer);
        Assert.assertNotNull(customer.getId());
    }

    @Test
    public void testGetAndUpdateCustomer() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        Customer customer = Customer.get(customerId);
        Assert.assertNotNull(customer);
        customer.setName("Juanito 2");
        customer = Customer.update(customer);
        Assert.assertEquals("Juanito 2", customer.getName());
        customer.setName("Juanito");
        customer = Customer.update(customer);
        Assert.assertEquals("Juanito", customer.getName());
    }

    @Test
    public void testGetList() throws ServiceUnavailableException, OpenpayServiceException {
        List<Customer> customers = Customer.list(search().offset(0).limit(100));
        Assert.assertNotNull(customers);
        for (Customer customer : customers) {
            Assert.assertNotNull(customer.getId());
            Assert.assertNotNull(customer.getBalance());
            Assert.assertNotNull(customer.getCreationDate());
            Assert.assertNotNull(customer.getEmail());
            Assert.assertNotNull(customer.getName());
            Assert.assertNotNull(customer.getStatus());
            Assert.assertNotNull(customer.getAddress());
        }
    }

    private Address createAddress() {
        Address address = new Address();
        address.setCity("Distrito Federal");
        address.setExteriorNumber("11");
        address.setInteriorNumber("01");
        address.setPostalCode("12345");
        address.setRegion("Naucalpan");
        address.setStreet("Camino Real");
        address.setState("Queretaro");
        return address;
    }

}
