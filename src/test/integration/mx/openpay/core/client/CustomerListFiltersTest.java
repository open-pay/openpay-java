/*
 * Copyright 2013 Opencard Inc.
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
package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.Customer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.CustomerOperations;
import mx.openpay.client.core.requests.customer.CreateCustomerParams;
import mx.openpay.core.client.test.TestUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author elopez
 */
@Slf4j
public class CustomerListFiltersTest {

    static CustomerOperations customerOps;

    static List<Customer> createdCustomers;

    private static final int customersToCreate = 10;

    @BeforeClass
    public static void setUp() throws Exception {
        customerOps = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID).customers();
        createdCustomers = new ArrayList<Customer>();
        for (int i = 0; i < customersToCreate; i++) {
            createdCustomers.add(customerOps.create(
                    new CreateCustomerParams()
                            .name("Nombre")
                            .lastName("Last name")
                            .email("customer" + System.currentTimeMillis() + "@opencard.mx")
                            .phoneNumber("0000000000")
                            .address(TestUtils.prepareAddress())));
            Thread.sleep(1000);
        }
        Collections.reverse(createdCustomers);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        for (Customer customer : createdCustomers) {
            try {
                customerOps.delete(customer.getId());
            } catch (Exception e) {
                log.warn("Could not delete customer " + customer.getId());
            }
        }
    }

    @Test
    public void testList() throws Exception {
        List<Customer> customers = customerOps.list(null);
        assertEquals(10, customers.size());
        for (int i = 0; i < customers.size(); i++) {
            assertEquals(createdCustomers.get(i).getId(), customers.get(i).getId());
        }
    }

    @Test
    public void testList_Limit() throws Exception {
        List<Customer> customers = customerOps.list(search().limit(2));
        assertEquals(2, customers.size());
        for (int i = 0; i < customers.size(); i++) {
            assertEquals(createdCustomers.get(i).getId(), customers.get(i).getId());
        }
    }

    @Test
    public void testList_Offset() throws Exception {
        List<Customer> customers = customerOps.list(search().offset(1));
        assertEquals(10, customers.size());
        for (int i = 1; i < createdCustomers.size(); i++) {
            assertEquals(createdCustomers.get(i).getId(), customers.get(i - 1).getId());
        }
    }

    @Test
    public void testList_Offset_Limit() throws Exception {
        List<Customer> customers = customerOps.list(search().offset(1).limit(1));
        assertEquals(1, customers.size());
        assertEquals(createdCustomers.get(1).getId(), customers.get(0).getId());
    }

    @Test
    public void testList_Create() throws Exception {
        List<Customer> customers = customerOps.list(search().creation(new Date()));
        assertEquals(10, customers.size());
        for (int i = 0; i < customers.size(); i++) {
            assertEquals(createdCustomers.get(i).getId(), customers.get(i).getId());
        }
    }

    @Test
    public void testList_Create_Offset() throws Exception {
        List<Customer> customers = customerOps.list(search().creation(new Date()).offset(1));
        assertEquals(9, customers.size());
        for (int i = 1; i < createdCustomers.size(); i++) {
            assertEquals(createdCustomers.get(i).getId(), customers.get(i - 1).getId());
        }
    }

    @Test
    public void testList_Create_Between_Inverted() throws Exception {
        Date start = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        Date end = c.getTime();
        List<Customer> customers = customerOps.list(search().between(end, start));
        assertEquals(0, customers.size());
    }

}
