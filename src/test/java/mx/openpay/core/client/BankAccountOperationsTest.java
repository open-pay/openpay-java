package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import mx.openpay.client.BankAccount;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BankAccountOperationsTest {

    @Before
    public void setUp() throws Exception {
        OpenpayAPI.configure(ENDPOINT, API_KEY, MERCHANT_ID);
    }

    @Test
    public void testCreateAndDeleteBankAccount() throws ServiceUnavailable, HttpError {
        String customerId = "afk4csrazjp1udezj1po";
        BankAccount bank = BankAccount.create(customerId, "012298026516924616", "Mi nombre", null);
        Assert.assertNotNull(bank);
        BankAccount.delete(customerId, bank.getId());
    }

    @Test
    public void testGetBankAccounts() throws ServiceUnavailable, HttpError {
        String customerId = "afk4csrazjp1udezj1po";
        List<BankAccount> banksAccounts = BankAccount.getList(customerId, search().offset(0).limit(100));
        Assert.assertNotNull(banksAccounts);
        for (BankAccount bankAccount : banksAccounts) {
            Assert.assertNotNull(bankAccount);
            Assert.assertNotNull(bankAccount.getId());
        }
    }

    @Test
    public void testGetBankAccount() throws Exception {
        BankAccount bankAccount = BankAccount.get("afk4csrazjp1udezj1po", "b6bhqhlewbbtqz1ga7aq");
        assertNotNull(bankAccount);
        assertEquals("012680012570003085", bankAccount.getClabe());
    }

    @Test
    public void testCreateBankAccount_ClabeAlreadyExists() throws ServiceUnavailable, HttpError {
        String customerId = "afk4csrazjp1udezj1po";
        try {
            BankAccount.create(customerId, "012680012570003085", "mi nombre", null);
            Assert.fail("Bank Account should be exists.");
        } catch (HttpError e) {
            Assert.assertEquals(409, e.getHttpCode().intValue());
            String bankId = "b6bhqhlewbbtqz1ga7aq";
            BankAccount account = BankAccount.get(customerId, bankId);
            Assert.assertNotNull(account);
            Assert.assertNotNull(account.getId());
            Assert.assertNotNull(account.getBankName());
            Assert.assertEquals("BANCOMER", account.getBankName());
        }
    }

}
