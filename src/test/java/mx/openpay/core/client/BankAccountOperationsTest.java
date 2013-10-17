package mx.openpay.core.client;

import java.util.List;

import mx.openpay.client.BankAccount;
import mx.openpay.client.OpenPayServices;
import mx.openpay.client.core.OpenPayServicesImpl;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BankAccountOperationsTest {

    private OpenPayServices openPayServices;

    private static String root = "http://openpay-sandbox-api.elasticbeanstalk.com";

    private String customerId = "m7psroutl8tycqtcmxly";

    private String apiKey = "e97b8bf7728242c0aa97b409a4c59236";

    @Before
    public void setUp() throws Exception {
        this.openPayServices = new OpenPayServicesImpl(this.customerId, this.apiKey, root);
    }

    @Test
    public void testCreateBankAccount() throws ServiceUnavailable, HttpError {
        String customerId = "agt0tslutb7tyz4nu1ce";
        try {
            this.openPayServices.createBank(customerId, "012680012570003085");
            Assert.fail("Bank Account should be exists.");
        } catch (HttpError e) {
            Assert.assertEquals(409, e.getHttpCode().intValue());
            String bankId = "basok4hygm60d4mepsg6";
            BankAccount account = this.openPayServices.getBank(customerId, bankId);
            Assert.assertNotNull(account);
            Assert.assertNotNull(account.getId());
            Assert.assertNotNull(account.getBankName());
            Assert.assertEquals("BANCOMER", account.getBankName());
        }
    }

    @Test
    public void testGetBankAccounts() throws ServiceUnavailable, HttpError {
        String customerId = "agt0tslutb7tyz4nu1ce";
        List<BankAccount> banksAccounts = this.openPayServices.getBankAccounts(customerId, 0, 100);
        Assert.assertNotNull(banksAccounts);
        for (BankAccount bankAccount : banksAccounts) {
            Assert.assertNotNull(bankAccount);
            Assert.assertNotNull(bankAccount.getId());
        }
    }

    @Test
    public void testGetBankAccount() throws ServiceUnavailable, HttpError {
        String customerId = "agt0tslutb7tyz4nu1ce";
        String bankId = "basok4hygm60d4mepsg6";
        BankAccount bank = this.openPayServices.getBank(customerId, bankId);
        Assert.assertNotNull(bank);

        bank = this.openPayServices.inactivateBank(customerId, bankId);
        Assert.assertNotNull(bank);
        Assert.assertEquals("INACTIVE", bank.getStatus());

        bank = this.openPayServices.activateBank(customerId, bankId);
        Assert.assertNotNull(bank);
        Assert.assertEquals("ACTIVE", bank.getStatus());
    }
}
