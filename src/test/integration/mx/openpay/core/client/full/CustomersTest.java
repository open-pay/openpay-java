/*

 * Copyright 2014 Opencard Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.openpay.core.client.full;

import static mx.openpay.client.utils.SearchParams.search;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import mx.openpay.client.Address;
import mx.openpay.client.Customer;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.core.client.test.TestUtils;

import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class CustomersTest extends BaseTest {

    private List<Customer> customersToDelete;

    @Before
    public void setUp() throws Exception {
        this.customersToDelete = new ArrayList<Customer>();
    }

    @After
    public void tearDown() throws Exception {
        for (Customer customer : this.customersToDelete) {
            this.api.customers().delete(customer.getId());
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
        Customer customer = this.api.customers().create(params);
        this.customersToDelete.add(customer);
        assertNotNull(customer);
        assertNotNull(customer.getId());
    }

    @Test
    public void testCreateCustomer_NoAccount() throws ServiceUnavailableException, OpenpayServiceException {
        Address address = TestUtils.prepareAddress();
        Customer params = new Customer()
                .name("Juan").lastName("Perez Perez")
                .email("juan.perez@gmail.com")
                .phoneNumber("55-25634013")
                .address(address)
                .requiresAccount(false);
        Customer customer = this.api.customers().create(params);
        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertNull(customer.getBalance());
        assertNull(customer.getStatus());
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        Address address = TestUtils.prepareAddress();
        Customer customer = this.api.customers().create(new Customer()
                .name("Juan").lastName("Perez Perez")
                .email("juan.perez@gmail.com")
                .phoneNumber("55-25634013")
                .address(address));
        this.customersToDelete.add(customer);
        customer.setName("Juanito 2");
        customer = this.api.customers().update(customer);
        assertEquals("Juanito 2", customer.getName());
    }

    @Test
    public void testUpdateCustomer_NoAccount() throws Exception {
        Address address = TestUtils.prepareAddress();
        Customer customer = this.api.customers().create(new Customer()
                .name("Juan").lastName("Perez Perez")
                .email("juan.perez@gmail.com")
                .phoneNumber("55-25634013")
                .address(address))
                .requiresAccount(false);
        this.customersToDelete.add(customer);
        customer.setName("Juanito 2");
        customer = this.api.customers().update(customer);
        assertEquals("Juanito 2", customer.getName());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateCustomer_Old() throws ServiceUnavailableException, OpenpayServiceException {
        Address address = TestUtils.prepareAddress();
        Customer customer = this.api.customers()
                .create("Juan", "Perez Perez", "juan.perez@gmail.com", "55-25634013", address);
        this.customersToDelete.add(customer);
        assertNotNull(customer);
        assertNotNull(customer.getId());
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        Address address = TestUtils.prepareAddress();
        Customer customer = this.api.customers().create(new Customer()
                .name("Juan").lastName("Perez Perez")
                .email("juan.perez@gmail.com")
                .phoneNumber("55-25634013")
                .address(address));
        this.api.customers().delete(customer.getId());
        try {
            this.api.customers().get(customer.getId());
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testDelete_DoesNotExist() throws Exception {
        try {
            this.api.customers().delete("blahblahblah");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testList() throws Exception {
        // To get next three customers first
        this.customersToDelete.add(this.api.customers().create(new Customer().name("Juan").lastName("Perez Perez")
                .email("juan.perez@gmail.com").phoneNumber("55-25634013").requiresAccount(false)));
        this.customersToDelete.add(this.api.customers().create(new Customer().name("Jose").lastName("Perez Perez")
                .email("jose.perez@gmail.com").phoneNumber("55-25634013").requiresAccount(false)));
        this.customersToDelete.add(this.api.customers().create(new Customer().name("Ruben").lastName("Perez Perez")
                .email("ruben.perez@gmail.com").phoneNumber("55-25634013").requiresAccount(false)));
        List<Customer> customers = this.api.customers().list(search().limit(3));
        assertThat(customers.size(), is(3));
        for (Customer customer : customers) {
            assertNotNull(customer.getId());
            assertNotNull(customer.getCreationDate());
            assertNotNull(customer.getEmail());
            assertNotNull(customer.getName());
            assertNull(customer.getBalance());
            assertNull(customer.getStatus());
        }
        customers = this.api.customers().list(search().limit(2));
        assertThat(customers.size(), is(2));
        Customer expectedFirst = customers.get(1);
        customers = this.api.customers().list(search().limit(2).offset(1));
        assertThat(customers.get(0).getId(), is(expectedFirst.getId()));
    }

    //@Test
    /*public void testList_OnlyWithoutAccount() throws ServiceUnavailableException, OpenpayServiceException {
        List<Customer> customers = this.api.customers().list(null);
        for (Customer customer : customers) {
            Assert.assertTrue(customer.getBalance() == null || customer.getBalance().compareTo(new BigDecimal("0.00")) == 0);
            if (customer.getStatus() != null) {
            	Assert.assertFalse(customer.getStatus().isEmpty());
            } else {
            	assertThat(customer.getStatus(), is(nullValue()));	
            }
        }
    }*/

    @Test
    public void testGet_DoesNotExist() throws Exception {
        try {
            this.api.customers().get("blahblahblah");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

}
