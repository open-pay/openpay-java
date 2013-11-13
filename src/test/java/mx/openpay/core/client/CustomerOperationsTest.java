package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.CUSTOMER_ID;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    public void testDelete_DoesNotExist() throws Exception {
        try {
            Customer.delete("blahblahblah");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testGetAndUpdateCustomer() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = CUSTOMER_ID;
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
        assertFalse(customers.isEmpty());
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

    @Test
    public void testGetList_Empty() throws ServiceUnavailableException, OpenpayServiceException {
        List<Customer> customers = Customer.list(search().offset(1000).limit(1));
        Assert.assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    public void testGet_DoesNotExist() throws Exception {
        try {
            Customer.get("blahblahblah");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
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
