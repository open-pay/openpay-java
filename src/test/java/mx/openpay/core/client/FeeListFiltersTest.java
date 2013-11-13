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

import mx.openpay.client.Fee;
import mx.openpay.client.core.OpenpayAPI;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
public class FeeListFiltersTest {

    @Before
    public void setUp() throws Exception {
        OpenpayAPI.configure(ENDPOINT, API_KEY, MERCHANT_ID);
    }

    @Test
    public void testSearch() throws Exception {
        List<Fee> bankAccounts = Fee.list(null);
        assertEquals(10, bankAccounts.size());
        assertEquals("alsbga3kduomgwyvlrwz", bankAccounts.get(0).getId());
        assertEquals("agdn58ngcnogqmzruz1i", bankAccounts.get(1).getId());
        assertEquals("a5woid02joj9zidld8oh", bankAccounts.get(2).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().before(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_Limit() throws Exception {
        List<Fee> bankAccounts = Fee.list(search().limit(2));
        assertEquals(2, bankAccounts.size());
        assertEquals("alsbga3kduomgwyvlrwz", bankAccounts.get(0).getId());
        assertEquals("agdn58ngcnogqmzruz1i", bankAccounts.get(1).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().before(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_Offset() throws Exception {
        List<Fee> bankAccounts = Fee.list(search().offset(1));
        assertEquals(10, bankAccounts.size());
        assertEquals("agdn58ngcnogqmzruz1i", bankAccounts.get(0).getId());
        assertEquals("a5woid02joj9zidld8oh", bankAccounts.get(1).getId());
        assertEquals("atgtivxs16jvpb5qavsa", bankAccounts.get(9).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().before(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_Offset_Limit() throws Exception {
        List<Fee> bankAccounts = Fee.list(search().offset(1).limit(1));
        assertEquals(1, bankAccounts.size());
        assertEquals("agdn58ngcnogqmzruz1i", bankAccounts.get(0).getId());
    }

    @Test
    public void testSearch_Create() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-05");
        List<Fee> bankAccounts = Fee.list(search().creation(date));
        assertEquals(2, bankAccounts.size());
        assertEquals("axkoqkqckvqd4wpmjj7z", bankAccounts.get(0).getId());
        assertEquals("aidzidphdseqwhfu0yjo", bankAccounts.get(1).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().before(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_Create_Offset() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-05");
        List<Fee> bankAccounts = Fee.list(search().creation(date).offset(1));
        assertEquals(1, bankAccounts.size());
        assertEquals("aidzidphdseqwhfu0yjo", bankAccounts.get(0).getId());
    }

    @Test
    public void testSearch_CreateLte() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-01");
        List<Fee> bankAccounts = Fee.list(search().creationLte(date));
        assertEquals(10, bankAccounts.size());
        assertEquals("alsbga3kduomgwyvlrwz", bankAccounts.get(0).getId());
        assertEquals("agdn58ngcnogqmzruz1i", bankAccounts.get(1).getId());
        assertEquals("a5woid02joj9zidld8oh", bankAccounts.get(2).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().before(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_CreateLte_NoStartOfNextDay() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-10-31");
        List<Fee> bankAccounts = Fee.list(search().creationLte(date));
        assertEquals(1, bankAccounts.size());
        assertEquals("alsbga3kduomgwyvlrwz", bankAccounts.get(0).getId());
    }

    @Test
    public void testSearch_CreateGte() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-03");
        List<Fee> bankAccounts = Fee.list(search().creationGte(date));
        assertEquals(4, bankAccounts.size());
        assertEquals("afk4csrazjp1udezj1po", bankAccounts.get(0).getId());
        assertEquals("axkoqkqckvqd4wpmjj7z", bankAccounts.get(1).getId());
        assertEquals("aidzidphdseqwhfu0yjo", bankAccounts.get(2).getId());
        assertEquals("aip6wu2pujiyfvm3urlp", bankAccounts.get(3).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().before(bankAccounts.get(1).getCreationDate()));
        assertTrue(bankAccounts.get(1).getCreationDate().before(bankAccounts.get(2).getCreationDate()));
        assertTrue(bankAccounts.get(2).getCreationDate().before(bankAccounts.get(3).getCreationDate()));
    }

    @Test
    public void testSearch_CreateGte_StartOfCurrentDay() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-05");
        List<Fee> bankAccounts = Fee.list(search().creationGte(date));
        assertEquals(3, bankAccounts.size());
        assertEquals("axkoqkqckvqd4wpmjj7z", bankAccounts.get(0).getId());
        assertEquals("aidzidphdseqwhfu0yjo", bankAccounts.get(1).getId());
        assertEquals("aip6wu2pujiyfvm3urlp", bankAccounts.get(2).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().before(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_Create_Between() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-05");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-05");
        List<Fee> bankAccounts = Fee.list(search().between(start, end));
        assertEquals(2, bankAccounts.size());
        assertEquals("axkoqkqckvqd4wpmjj7z", bankAccounts.get(0).getId());
        assertEquals("aidzidphdseqwhfu0yjo", bankAccounts.get(1).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().before(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_Create_Between_FirstFee() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-03");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-12");
        List<Fee> bankAccounts = Fee.list(search().between(start, end).limit(1));
        assertEquals(1, bankAccounts.size());
        assertEquals("afk4csrazjp1udezj1po", bankAccounts.get(0).getId());
    }

    @Test
    public void testSearch_Create_Between_Inverted() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-03");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-12");
        List<Fee> bankAccounts = Fee.list(search().between(end, start));
        assertEquals(0, bankAccounts.size());
    }

}
