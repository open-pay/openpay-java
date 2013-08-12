package mx.openpay.core;

import java.util.List;

import mx.openpay.BankAccount;
import mx.openpay.OpenPayServices;
import mx.openpay.exceptions.HttpError;
import mx.openpay.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BankAccountOperationsTest {

    private OpenPayServices openPayServices;

    private static String root = "http://localhost:8081/Services";

    private String customerId = "hgqemgk8g368fqw79i35";

    private String apiKey = "5eb59e956b614015b0a81cb311b892f4";

    @Before
    public void setUp() throws Exception {
        this.openPayServices = new OpenPayServicesImpl(this.customerId, this.apiKey, root);
    }

    @Test
    public void testCreateBankAccount() throws ServiceUnavailable, HttpError {
        String ewalletId = "ls0jzlyrwvjqm1kk3vwg";
        try {
            this.openPayServices.createBank(ewalletId, "012680012570003085");
            Assert.fail("Bank Account should be exists.");
        } catch (HttpError e) {
            Assert.assertEquals(409, e.getHttpCode().intValue());
            String bankId = "gwe2pjtmmb2omkhrl8fo";
            BankAccount account = this.openPayServices.getBank(ewalletId, bankId);
            Assert.assertNotNull(account);
            Assert.assertNotNull(account.getId());
            Assert.assertNotNull(account.getBank());
            Assert.assertEquals("BANCOMER", account.getBank().getName());
        }
    }

    @Test
    public void testGetBankAccounts() throws ServiceUnavailable, HttpError {
        String ewalletId = "ls0jzlyrwvjqm1kk3vwg";
        List<BankAccount> banksAccounts = this.openPayServices.getBankAccounts(ewalletId, 0, 100);
        Assert.assertNotNull(banksAccounts);
        for (BankAccount bankAccount : banksAccounts) {
            Assert.assertNotNull(bankAccount);
            Assert.assertNotNull(bankAccount.getId());
        }
    }

    @Test
    public void testGetBankAccount() throws ServiceUnavailable, HttpError {
        String ewalletId = "ls0jzlyrwvjqm1kk3vwg";
        String bankId = "gwe2pjtmmb2omkhrl8fo";
        BankAccount bank = this.openPayServices.getBank(ewalletId, bankId);
        Assert.assertNotNull(bank);

        bank = this.openPayServices.inactivateBank(ewalletId, bankId);
        Assert.assertNotNull(bank);
        Assert.assertEquals("INACTIVE", bank.getStatus());

        bank = this.openPayServices.activateBank(ewalletId, bankId);
        Assert.assertNotNull(bank);
        Assert.assertEquals("ACTIVE", bank.getStatus());
    }
}
