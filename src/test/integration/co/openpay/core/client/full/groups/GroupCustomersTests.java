package co.openpay.core.client.full.groups;

import static co.openpay.client.utils.SearchParams.search;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import co.openpay.client.Address;
import co.openpay.client.Customer;
import co.openpay.client.exceptions.OpenpayServiceException;
import co.openpay.client.exceptions.ServiceUnavailableException;
import co.openpay.core.client.test.TestUtils;

/** Tests to create customers belonging to a group. */
public class GroupCustomersTests extends GroupBaseTest {

    private List<Customer> customersToDelete;

    @Before
    public void setUp() throws Exception {
        this.customersToDelete = new ArrayList<Customer>();
    }

    @After
    public void tearDown() throws Exception {
        for (Customer customer : this.customersToDelete) {
            this.groupApi.groupCustomers().delete(customer.getId());
        }
    }

    @Test
    public void testCreateCustomer() throws ServiceUnavailableException, OpenpayServiceException {
        Address address = TestUtils.prepareAddress();
        Customer params = new Customer()
                .name("Juan").lastName("Perez Perez")
                .email("juan.perez@gmail.com")
                .phoneNumber("55-25634013")
                .address(address);
        Customer customer = this.groupApi.groupCustomers().create(params);
        this.customersToDelete.add(customer);
        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertNull(customer.getBalance());
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        Address address = TestUtils.prepareAddress();
        Customer customer = this.groupApi.groupCustomers().create(new Customer()
                .name("Juan").lastName("Perez Perez")
                .email("juan.perez@gmail.com")
                .phoneNumber("55-25634013")
                .address(address));
        this.customersToDelete.add(customer);
        customer.setName("Juanito 2");
        customer = this.groupApi.groupCustomers().update(customer);
        assertEquals("Juanito 2", customer.getName());
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        Address address = TestUtils.prepareAddress();
        Customer customer = this.groupApi.groupCustomers().create(new Customer()
                .name("Juan").lastName("Perez Perez")
                .email("juan.perez@gmail.com")
                .phoneNumber("55-25634013")
                .address(address));
        this.groupApi.groupCustomers().delete(customer.getId());
        try {
            this.groupApi.groupCustomers().get(customer.getId());
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testDelete_DoesNotExist() throws Exception {
        try {
            this.groupApi.groupCustomers().delete("blahblahblah");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testList() throws Exception {
        // To get next three customers first
        Thread.sleep(1000);
        this.customersToDelete
                .add(this.groupApi.groupCustomers().create(new Customer().name("Juan").lastName("Perez Perez")
                        .email("juan.perez@gmail.com").phoneNumber("55-25634013")));
        this.customersToDelete
                .add(this.groupApi.groupCustomers().create(new Customer().name("Jose").lastName("Perez Perez")
                        .email("jose.perez@gmail.com").phoneNumber("55-25634013")));
        this.customersToDelete
                .add(this.groupApi.groupCustomers().create(new Customer().name("Ruben").lastName("Perez Perez")
                        .email("ruben.perez@gmail.com").phoneNumber("55-25634013")));
        List<Customer> customers = this.groupApi.groupCustomers().list(search().limit(3));
        assertThat(customers.size(), is(3));
        for (Customer customer : customers) {
            assertNotNull(customer.getId());
            assertNotNull(customer.getCreationDate());
            assertNotNull(customer.getEmail());
            assertNotNull(customer.getName());
            assertNull(customer.getBalance());
        }
        customers = this.groupApi.groupCustomers().list(search().limit(2));
        assertThat(customers.size(), is(2));
        Customer expectedFirst = customers.get(1);
        customers = this.groupApi.groupCustomers().list(search().limit(2).offset(1));
        assertThat(customers.get(0).getId(), is(expectedFirst.getId()));
    }

    @Test
    public void testList_OnlyWithoutAccount() throws ServiceUnavailableException, OpenpayServiceException {
        List<Customer> customers = this.groupApi.groupCustomers().list(null);
        for (Customer customer : customers) {
            assertTrue(customer.getBalance() == null || customer.getBalance().equals(new BigDecimal("0.00")));
            if (customer.getStatus() != null) {
                assertFalse(customer.getStatus().isEmpty());
            } else {
                assertThat(customer.getStatus(), is(nullValue()));
            }
        }
    }

    @Test
    public void testGet_DoesNotExist() throws Exception {
        try {
            this.groupApi.groupCustomers().get("blahblahblah");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

}
