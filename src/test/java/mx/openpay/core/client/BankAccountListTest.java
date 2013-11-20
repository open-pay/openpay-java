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

import mx.openpay.client.BankAccount;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.BankAccountOperations;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
public class BankAccountListTest {

    BankAccountOperations bankAccountOps;

    @Before
    public void setUp() throws Exception {
        this.bankAccountOps = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID).bankAccounts();
    }

    @Test
    public void testList() throws Exception {
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", null);
        assertEquals(3, bankAccounts.size());
        assertEquals("bnaadrgz6ql9b3xw4pxa", bankAccounts.get(0).getId());
        assertEquals("bbpfsaibpuk56cyvx2k6", bankAccounts.get(1).getId());
        assertEquals("b6bhqhlewbbtqz1ga7aq", bankAccounts.get(2).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().after(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testList_Limit() throws Exception {
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", search().limit(2));
        assertEquals(2, bankAccounts.size());
        assertEquals("bnaadrgz6ql9b3xw4pxa", bankAccounts.get(0).getId());
        assertEquals("bbpfsaibpuk56cyvx2k6", bankAccounts.get(1).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().after(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testList_Offset() throws Exception {
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", search().offset(1));
        assertEquals(2, bankAccounts.size());
        assertEquals("bbpfsaibpuk56cyvx2k6", bankAccounts.get(0).getId());
        assertEquals("b6bhqhlewbbtqz1ga7aq", bankAccounts.get(1).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().after(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testList_Offset_Limit() throws Exception {
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", search().offset(1).limit(1));
        assertEquals(1, bankAccounts.size());
        assertEquals("bbpfsaibpuk56cyvx2k6", bankAccounts.get(0).getId());
    }

    @Test
    public void testList_Create() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-13");
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", search().creation(date));
        assertEquals(2, bankAccounts.size());
        assertEquals("bnaadrgz6ql9b3xw4pxa", bankAccounts.get(0).getId());
        assertEquals("bbpfsaibpuk56cyvx2k6", bankAccounts.get(1).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().after(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testList_Create_Offset() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-13");
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po",
                search().creation(date).offset(1));
        assertEquals(1, bankAccounts.size());
        assertEquals("bbpfsaibpuk56cyvx2k6", bankAccounts.get(0).getId());
    }

    @Test
    public void testList_CreateLte() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-13");
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", search().creationLte(date));
        assertEquals(3, bankAccounts.size());
        assertEquals("bnaadrgz6ql9b3xw4pxa", bankAccounts.get(0).getId());
        assertEquals("bbpfsaibpuk56cyvx2k6", bankAccounts.get(1).getId());
        assertEquals("b6bhqhlewbbtqz1ga7aq", bankAccounts.get(2).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().after(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testList_CreateLte_NoStartOfNextDay() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-12");
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", search().creationLte(date));
        assertEquals(1, bankAccounts.size());
        assertEquals("b6bhqhlewbbtqz1ga7aq", bankAccounts.get(0).getId());
    }

    @Test
    public void testList_CreateGte() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-01");
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", search().creationGte(date));
        assertEquals(3, bankAccounts.size());
        assertEquals("bnaadrgz6ql9b3xw4pxa", bankAccounts.get(0).getId());
        assertEquals("bbpfsaibpuk56cyvx2k6", bankAccounts.get(1).getId());
        assertEquals("b6bhqhlewbbtqz1ga7aq", bankAccounts.get(2).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().after(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testList_CreateGte_StartOfCurrentDay() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-13");
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", search().creationGte(date));
        assertEquals(2, bankAccounts.size());
        assertEquals("bnaadrgz6ql9b3xw4pxa", bankAccounts.get(0).getId());
        assertEquals("bbpfsaibpuk56cyvx2k6", bankAccounts.get(1).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().after(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testList_Create_Between() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-13");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-13");
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", search().between(start, end));
        assertEquals(2, bankAccounts.size());
        assertEquals("bnaadrgz6ql9b3xw4pxa", bankAccounts.get(0).getId());
        assertEquals("bbpfsaibpuk56cyvx2k6", bankAccounts.get(1).getId());
        assertTrue(bankAccounts.get(0).getCreationDate().after(bankAccounts.get(1).getCreationDate()));
    }

    @Test
    public void testList_Create_Between_FirstBankAccount() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-01");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-12");
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", search().between(start, end));
        assertEquals(1, bankAccounts.size());
        assertEquals("b6bhqhlewbbtqz1ga7aq", bankAccounts.get(0).getId());
    }

    @Test
    public void testList_Create_Between_Inverted() throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-01");
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2013-11-12");
        List<BankAccount> bankAccounts = this.bankAccountOps.list("afk4csrazjp1udezj1po", search().between(end, start));
        assertEquals(0, bankAccounts.size());
    }

}
