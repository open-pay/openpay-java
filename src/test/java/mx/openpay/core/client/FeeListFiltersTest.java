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
        List<Fee> fees = Fee.list(null);
        assertEquals(10, fees.size());
        assertEquals("thzktlymz07oecafrw6b", fees.get(0).getId());
        assertEquals("tmsekzj42hrtf95rgarh", fees.get(1).getId());
        assertEquals("t4n68tya07icqrlhiqkq", fees.get(2).getId());
        assertTrue(fees.get(0).getCreationDate().before(fees.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_Limit() throws Exception {
        List<Fee> fees = Fee.list(search().limit(2));
        assertEquals(2, fees.size());
        assertEquals("thzktlymz07oecafrw6b", fees.get(0).getId());
        assertEquals("tmsekzj42hrtf95rgarh", fees.get(1).getId());
        assertTrue(fees.get(0).getCreationDate().before(fees.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_Offset() throws Exception {
        List<Fee> fees = Fee.list(search().offset(1));
        assertEquals(10, fees.size());
        assertEquals("tmsekzj42hrtf95rgarh", fees.get(0).getId());
        assertEquals("t4n68tya07icqrlhiqkq", fees.get(1).getId());
        assertEquals("t9vzrnu17icqkm8z3pfp", fees.get(9).getId());
        assertTrue(fees.get(0).getCreationDate().before(fees.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_Offset_Limit() throws Exception {
        List<Fee> fees = Fee.list(search().offset(1).limit(1));
        assertEquals(1, fees.size());
        assertEquals("tmsekzj42hrtf95rgarh", fees.get(0).getId());
    }

    @Test
    public void testSearch_Creation() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-01");
        List<Fee> fees = Fee.list(search().creation(date));
        assertEquals(2, fees.size());
        assertEquals("thzktlymz07oecafrw6b", fees.get(0).getId());
        assertEquals("tmsekzj42hrtf95rgarh", fees.get(1).getId());
        assertTrue(fees.get(0).getCreationDate().before(fees.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_Creation_Offset() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-01");
        List<Fee> fees = Fee.list(search().creation(date).offset(1));
        assertEquals(1, fees.size());
        assertEquals("tmsekzj42hrtf95rgarh", fees.get(0).getId());
    }

    @Test
    public void testSearch_CreationLte() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-11");
        List<Fee> fees = Fee.list(search().creationLte(date));
        assertEquals(10, fees.size());
        assertEquals("thzktlymz07oecafrw6b", fees.get(0).getId());
        assertEquals("tmsekzj42hrtf95rgarh", fees.get(1).getId());
        assertEquals("t4n68tya07icqrlhiqkq", fees.get(2).getId());
        assertTrue(fees.get(0).getCreationDate().before(fees.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_CreationLte_NoStartOfNextDay() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-08");
        List<Fee> fees = Fee.list(search().creationLte(date));
        assertEquals(7, fees.size());
        assertEquals("thzktlymz07oecafrw6b", fees.get(0).getId());
        assertEquals("tmsekzj42hrtf95rgarh", fees.get(1).getId());
        assertEquals("tyfsbjhuqhqbicbeneem", fees.get(6).getId());
    }

    @Test
    public void testSearch_CreationGte() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-08");
        List<Fee> fees = Fee.list(search().creationGte(date));
        assertEquals(10, fees.size());
        assertEquals("t4n68tya07icqrlhiqkq", fees.get(0).getId());
        assertEquals("tx2vmjr1bgsf7soqsiyu", fees.get(1).getId());
        assertEquals("thzjlados5xjbymsiqgz", fees.get(2).getId());
        assertEquals("twighetvkucgbonp6yko", fees.get(3).getId());
        assertTrue(fees.get(0).getCreationDate().before(fees.get(1).getCreationDate()));
        assertTrue(fees.get(1).getCreationDate().before(fees.get(2).getCreationDate()));
        assertTrue(fees.get(2).getCreationDate().before(fees.get(3).getCreationDate()));
    }

    @Test
    public void testSearch_Creation_Between() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-08");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-09");
        List<Fee> fees = Fee.list(search().between(start, end));
        assertEquals(6, fees.size());
        assertEquals("t4n68tya07icqrlhiqkq", fees.get(0).getId());
        assertEquals("tx2vmjr1bgsf7soqsiyu", fees.get(1).getId());
        assertEquals("twxqhe7catm1j09mpzo6", fees.get(5).getId());
        assertTrue(fees.get(0).getCreationDate().before(fees.get(1).getCreationDate()));
    }

    @Test
    public void testSearch_Creation_Between_SameDay() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-09");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-19");
        List<Fee> fees = Fee.list(search().between(start, end).limit(1));
        assertEquals(1, fees.size());
        assertEquals("twxqhe7catm1j09mpzo6", fees.get(0).getId());
    }

    @Test
    public void testSearch_Creation_Between_Inverted() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-03");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-12");
        List<Fee> fees = Fee.list(search().between(end, start));
        assertEquals(0, fees.size());
    }

}
