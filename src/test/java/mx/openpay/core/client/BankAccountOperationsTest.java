package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import mx.openpay.client.BankAccount;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.BankAccountOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BankAccountOperationsTest {

    BankAccountOperations bankAccountOps;

    @Before
    public void setUp() throws Exception {
        this.bankAccountOps = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID).bankAccounts();
    }

    @Test
    public void testCreateAndDeleteBankAccount() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        BankAccount bank = this.bankAccountOps.create(customerId, "012298026516924616", "Mi nombre", null);
        Assert.assertNotNull(bank);
        this.bankAccountOps.delete(customerId, bank.getId());
    }

    @Test
    public void testDelete_DoesNotExist() throws Exception {
        try {
            this.bankAccountOps.delete("afk4csrazjp1udezj1po", "fesf4fcsdf");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testList() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        List<BankAccount> banksAccounts = this.bankAccountOps.list(customerId, search().offset(0).limit(100));
        Assert.assertNotNull(banksAccounts);
        Assert.assertTrue(banksAccounts.size() > 0);
        for (BankAccount bankAccount : banksAccounts) {
            Assert.assertNotNull(bankAccount);
            Assert.assertNotNull(bankAccount.getId());
        }
    }

    @Test
    public void testList_Empty() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        List<BankAccount> banksAccounts = this.bankAccountOps.list(customerId, search().offset(1000).limit(3));
        Assert.assertNotNull(banksAccounts);
        Assert.assertTrue(banksAccounts.isEmpty());
    }

    @Test
    public void testList_CustomerDoesNotExist() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = "asdsdsrazjp1udezj1po";
        try {
            this.bankAccountOps.list(customerId, search().offset(1000).limit(3));
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testGet() throws Exception {
        BankAccount bankAccount = this.bankAccountOps.get("afk4csrazjp1udezj1po", "b6bhqhlewbbtqz1ga7aq");
        assertNotNull(bankAccount);
        assertEquals("012680012570003085", bankAccount.getClabe());
    }

    @Test
    public void testGet_NotFound() throws Exception {
        try {
            this.bankAccountOps.get("sfdsf4r4", "sdffsdfs");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testCreateBankAccount_ClabeAlreadyExists() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        try {
            this.bankAccountOps.create(customerId, "012680012570003085", "mi nombre", null);
            Assert.fail("Bank Account should be exists.");
        } catch (OpenpayServiceException e) {
            Assert.assertEquals(409, e.getHttpCode().intValue());
            String bankId = "b6bhqhlewbbtqz1ga7aq";
            BankAccount account = this.bankAccountOps.get(customerId, bankId);
            Assert.assertNotNull(account);
            Assert.assertNotNull(account.getId());
            Assert.assertNotNull(account.getBankName());
            Assert.assertEquals("BANCOMER", account.getBankName());
        }
    }

}
