/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
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

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.Customer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.CustomerOperations;
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
            createdCustomers.add(customerOps.create("Nombre", "Last name", "customer" + System.currentTimeMillis()
                    + "@opencard.mx", "0000000000", TestUtils.prepareAddress()));
            Thread.sleep(1000);
        }
        Collections.reverse(createdCustomers);
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
