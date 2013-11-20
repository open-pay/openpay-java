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
import mx.openpay.client.core.operations.FeeOperations;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
public class FeeListFiltersTest {

    FeeOperations ops;

    @Before
    public void setUp() throws Exception {
        this.ops = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID).fees();
    }

    @Test
    public void testList() throws Exception {
        List<Fee> fees = this.ops.list(null);
        assertEquals(10, fees.size());
        assertEquals("t839jgbhaixbnpn1u99k", fees.get(0).getId());
        assertEquals("tut29lkf7ixtr5jlhcrs", fees.get(1).getId());
        assertEquals("tzbamdjkgf5im6lnurye", fees.get(2).getId());
        assertTrue(fees.get(0).getCreationDate().after(fees.get(1).getCreationDate()));
    }

    @Test
    public void testList_Limit() throws Exception {
        List<Fee> fees = this.ops.list(search().limit(2));
        assertEquals(2, fees.size());
        assertEquals("t839jgbhaixbnpn1u99k", fees.get(0).getId());
        assertEquals("tut29lkf7ixtr5jlhcrs", fees.get(1).getId());
        assertTrue(fees.get(0).getCreationDate().after(fees.get(1).getCreationDate()));
    }

    @Test
    public void testList_Offset() throws Exception {
        List<Fee> fees = this.ops.list(search().offset(1));
        assertEquals(10, fees.size());
        assertEquals("tut29lkf7ixtr5jlhcrs", fees.get(0).getId());
        assertEquals("tzbamdjkgf5im6lnurye", fees.get(1).getId());
        assertEquals("tomouuxso54si1dyyopl", fees.get(9).getId());
        assertTrue(fees.get(0).getCreationDate().after(fees.get(1).getCreationDate()));
    }

    @Test
    public void testList_Offset_Limit() throws Exception {
        List<Fee> fees = this.ops.list(search().offset(1).limit(1));
        assertEquals(1, fees.size());
        assertEquals("tut29lkf7ixtr5jlhcrs", fees.get(0).getId());
    }

    @Test
    public void testList_Creation() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-01");
        List<Fee> fees = this.ops.list(search().creation(date));
        assertEquals(2, fees.size());
        assertEquals("tmsekzj42hrtf95rgarh", fees.get(0).getId());
        assertEquals("thzktlymz07oecafrw6b", fees.get(1).getId());
        assertTrue(fees.get(0).getCreationDate().after(fees.get(1).getCreationDate()));
    }

    @Test
    public void testList_Creation_Offset() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-01");
        List<Fee> fees = this.ops.list(search().creation(date).offset(1));
        assertEquals(1, fees.size());
        assertEquals("thzktlymz07oecafrw6b", fees.get(0).getId());
    }

    @Test
    public void testList_CreationLte() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-11");
        List<Fee> fees = this.ops.list(search().creationLte(date));
        assertEquals(10, fees.size());
        assertEquals("to5yawmsi3j5ady2ifcr", fees.get(0).getId());
        assertEquals("t9vzrnu17icqkm8z3pfp", fees.get(1).getId());
        assertEquals("ty3ulukgjxakhrstrjwx", fees.get(2).getId());
        assertTrue(fees.get(0).getCreationDate().after(fees.get(1).getCreationDate()));
    }

    @Test
    public void testList_CreationLte_NoStartOfNextDay() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-08");
        List<Fee> fees = this.ops.list(search().creationLte(date));
        assertEquals(7, fees.size());
        assertEquals("tyfsbjhuqhqbicbeneem", fees.get(0).getId());
        assertEquals("twighetvkucgbonp6yko", fees.get(1).getId());
        assertEquals("thzktlymz07oecafrw6b", fees.get(6).getId());
    }

    @Test
    public void testList_CreationGte() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-08");
        List<Fee> fees = this.ops.list(search().creationGte(date));
        assertEquals(10, fees.size());
        assertEquals("t839jgbhaixbnpn1u99k", fees.get(0).getId());
        assertEquals("tut29lkf7ixtr5jlhcrs", fees.get(1).getId());
        assertEquals("tzbamdjkgf5im6lnurye", fees.get(2).getId());
        assertEquals("tgfmskiyc0v96sz2aoe1", fees.get(3).getId());
        assertTrue(fees.get(0).getCreationDate().after(fees.get(1).getCreationDate()));
        assertTrue(fees.get(1).getCreationDate().after(fees.get(2).getCreationDate()));
        assertTrue(fees.get(2).getCreationDate().after(fees.get(3).getCreationDate()));
    }

    @Test
    public void testList_Creation_Between() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-08");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-09");
        List<Fee> fees = this.ops.list(search().between(start, end));
        assertEquals(6, fees.size());
        assertEquals("twxqhe7catm1j09mpzo6", fees.get(0).getId());
        assertEquals("tyfsbjhuqhqbicbeneem", fees.get(1).getId());
        assertEquals("t4n68tya07icqrlhiqkq", fees.get(5).getId());
        assertTrue(fees.get(0).getCreationDate().after(fees.get(1).getCreationDate()));
    }

    @Test
    public void testList_Creation_Between_SameDay() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-09");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-19");
        List<Fee> fees = this.ops.list(search().between(start, end).limit(1));
        assertEquals(1, fees.size());
        assertEquals("t839jgbhaixbnpn1u99k", fees.get(0).getId());
    }

    @Test
    public void testList_Creation_Between_Inverted() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-03");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-12");
        List<Fee> fees = this.ops.list(search().between(end, start));
        assertEquals(0, fees.size());
    }

}
