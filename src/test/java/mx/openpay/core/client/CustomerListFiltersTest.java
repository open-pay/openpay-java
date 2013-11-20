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
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mx.openpay.client.Customer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.CustomerOperations;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
public class CustomerListFiltersTest {

    CustomerOperations ops;

    @Before
    public void setUp() throws Exception {
        this.ops = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID).customers();
    }

    @Test
    public void testList() throws Exception {
        List<Customer> customers = this.ops.list(null);
        assertEquals(10, customers.size());
        assertEquals("aip6wu2pujiyfvm3urlp", customers.get(0).getId());
        assertEquals("aidzidphdseqwhfu0yjo", customers.get(1).getId());
        assertEquals("axkoqkqckvqd4wpmjj7z", customers.get(2).getId());
        assertTrue(customers.get(0).getCreationDate().after(customers.get(1).getCreationDate()));
    }

    @Test
    public void testList_Limit() throws Exception {
        List<Customer> customers = this.ops.list(search().limit(2));
        assertEquals(2, customers.size());
        assertEquals("aip6wu2pujiyfvm3urlp", customers.get(0).getId());
        assertEquals("aidzidphdseqwhfu0yjo", customers.get(1).getId());
        assertTrue(customers.get(0).getCreationDate().after(customers.get(1).getCreationDate()));
    }

    @Test
    public void testList_Offset() throws Exception {
        List<Customer> customers = this.ops.list(search().offset(1));
        assertEquals(10, customers.size());
        assertEquals("aidzidphdseqwhfu0yjo", customers.get(0).getId());
        assertEquals("axkoqkqckvqd4wpmjj7z", customers.get(1).getId());
        assertEquals("a7tluhknpei4h0j04krq", customers.get(9).getId());
        assertTrue(customers.get(0).getCreationDate().after(customers.get(1).getCreationDate()));
    }

    @Test
    public void testList_Offset_Limit() throws Exception {
        List<Customer> customers = this.ops.list(search().offset(1).limit(1));
        assertEquals(1, customers.size());
        assertEquals("aidzidphdseqwhfu0yjo", customers.get(0).getId());
    }

    @Test
    public void testList_Create() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-05");
        List<Customer> customers = this.ops.list(search().creation(date));
        assertEquals(2, customers.size());
        assertEquals("aidzidphdseqwhfu0yjo", customers.get(0).getId());
        assertEquals("axkoqkqckvqd4wpmjj7z", customers.get(1).getId());
        assertTrue(customers.get(0).getCreationDate().after(customers.get(1).getCreationDate()));
    }

    @Test
    public void testList_Create_Offset() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-05");
        List<Customer> customers = this.ops.list(search().creation(date).offset(1));
        assertEquals(1, customers.size());
        assertEquals("axkoqkqckvqd4wpmjj7z", customers.get(0).getId());
    }

    @Test
    public void testList_CreateLte() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-01");
        List<Customer> customers = this.ops.list(search().creationLte(date));
        assertEquals(10, customers.size());
        assertEquals("afvuzdsmeia7ykvcdygz", customers.get(0).getId());
        assertEquals("amgwgxv6ovtopljapecc", customers.get(1).getId());
        assertEquals("ah2gsusecghutbxkdesr", customers.get(2).getId());
        assertTrue(customers.get(0).getCreationDate().after(customers.get(1).getCreationDate()));
    }

    @Test
    public void testList_CreateLte_NoStartOfNextDay() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-10-31");
        List<Customer> customers = this.ops.list(search().creationLte(date));
        assertEquals(1, customers.size());
        assertEquals("alsbga3kduomgwyvlrwz", customers.get(0).getId());
    }

    @Test
    public void testList_CreateGte() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-03");
        List<Customer> customers = this.ops.list(search().creationGte(date));
        assertEquals(4, customers.size());
        assertEquals("aip6wu2pujiyfvm3urlp", customers.get(0).getId());
        assertEquals("aidzidphdseqwhfu0yjo", customers.get(1).getId());
        assertEquals("axkoqkqckvqd4wpmjj7z", customers.get(2).getId());
        assertEquals("afk4csrazjp1udezj1po", customers.get(3).getId());
        assertTrue(customers.get(0).getCreationDate().after(customers.get(1).getCreationDate()));
        assertTrue(customers.get(1).getCreationDate().after(customers.get(2).getCreationDate()));
        assertTrue(customers.get(2).getCreationDate().after(customers.get(3).getCreationDate()));
    }

    @Test
    public void testList_CreateGte_StartOfCurrentDay() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-05");
        List<Customer> customers = this.ops.list(search().creationGte(date));
        assertEquals(3, customers.size());
        assertEquals("aip6wu2pujiyfvm3urlp", customers.get(0).getId());
        assertEquals("aidzidphdseqwhfu0yjo", customers.get(1).getId());
        assertEquals("axkoqkqckvqd4wpmjj7z", customers.get(2).getId());
        assertTrue(customers.get(0).getCreationDate().after(customers.get(1).getCreationDate()));
    }

    @Test
    public void testList_Create_Between() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-05");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-05");
        List<Customer> customers = this.ops.list(search().between(start, end));
        assertEquals(2, customers.size());
        assertEquals("aidzidphdseqwhfu0yjo", customers.get(0).getId());
        assertEquals("axkoqkqckvqd4wpmjj7z", customers.get(1).getId());
        assertTrue(customers.get(0).getCreationDate().after(customers.get(1).getCreationDate()));
    }

    @Test
    public void testList_Create_Between_FirstCustomer() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-03");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-12");
        List<Customer> customers = this.ops.list(search().between(start, end).limit(1));
        assertEquals(1, customers.size());
        assertEquals("aip6wu2pujiyfvm3urlp", customers.get(0).getId());
    }

    @Test
    public void testList_Create_Between_Inverted() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-03");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-12");
        List<Customer> customers = this.ops.list(search().between(end, start));
        assertEquals(0, customers.size());
    }

}
