/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.core.client.full;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import mx.openpay.client.BankAccount;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class MerchantBankAccountsTest extends BaseTest {

    private List<BankAccount> bankAccountsToDelete;

    @Before
    public void setUp() throws Exception {
        this.bankAccountsToDelete = new ArrayList<BankAccount>();
    }

    @After
    public void tearDown() throws Exception {
        for (BankAccount bankAccount : this.bankAccountsToDelete) {
            this.api.bankAccounts().delete(bankAccount.getId());
        }
    }

    @Test
    public void testCreateMerchantBankAccount() throws ServiceUnavailableException, OpenpayServiceException {
        BankAccount bankAccount = this.api.bankAccounts().create(new BankAccount()
                .clabe("012298026516924616")
                .holderName("Mi nombre"));
        this.bankAccountsToDelete.add(bankAccount);
        Assert.assertNotNull(bankAccount);
    }

    @Test
    public void testDelete_DoesNotExist() throws Exception {
        try {
            this.api.bankAccounts().delete("fesf4fcsdf");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testList_Empty() throws ServiceUnavailableException, OpenpayServiceException {
        List<BankAccount> banksAccounts = this.api.bankAccounts().list(null);
        Assert.assertTrue(banksAccounts.isEmpty());
    }

    @Test
    public void testList() throws ServiceUnavailableException, OpenpayServiceException {
        this.bankAccountsToDelete.add(this.api.bankAccounts().create(new BankAccount()
                .clabe("032180000118359719").holderName("Mi nombre")));
        this.bankAccountsToDelete.add(this.api.bankAccounts().create(new BankAccount()
                .clabe("012298026516924616").holderName("Mi nombre")));
        this.bankAccountsToDelete.add(this.api.bankAccounts().create(new BankAccount()
                .clabe("646180109401000075").holderName("Mi nombre")));
        this.bankAccountsToDelete.add(this.api.bankAccounts().create(new BankAccount()
                .clabe("646180109400000089").holderName("Mi nombre")));
        List<BankAccount> bankAccounts = this.api.bankAccounts().list(null);
        assertThat(bankAccounts.size(), is(4));
        for (BankAccount bankAccount : bankAccounts) {
            Assert.assertNotNull(bankAccount);
            Assert.assertNotNull(bankAccount.getId());
        }
    }

    @Test
    public void testList_MerchantDoesNotExist() throws ServiceUnavailableException, OpenpayServiceException {
        try {
            this.api.bankAccounts().list("asdsdsrazjp1udezj1po", null);
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testDelete() throws Exception {
        BankAccount bankAccount = this.api.bankAccounts().create(new BankAccount()
                .clabe("012680026840227260").holderName("Mi nombre"));
        this.api.bankAccounts().delete(bankAccount.getId());
        try {
            bankAccount = this.api.bankAccounts().get(bankAccount.getId());
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testGet() throws Exception {
        BankAccount bankAccount = this.api.bankAccounts().create(new BankAccount()
                .clabe("012680026840227260").holderName("Mi nombre"));
        this.bankAccountsToDelete.add(bankAccount);
        bankAccount = this.api.bankAccounts().get(bankAccount.getId());
        assertEquals("012XXXXXXXXXX27260", bankAccount.getClabe());
    }

    @Test
    public void testGet_NotFound() throws Exception {
        try {
            this.api.bankAccounts().get("sdffsdfs");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testCreateBankAccount_ClabeAlreadyExists() throws ServiceUnavailableException, OpenpayServiceException {
        try {
            this.bankAccountsToDelete.add(this.api.bankAccounts().create(
                    new BankAccount().clabe("012680012570003085").holderName("mi nombre")));
            this.api.bankAccounts().create(new BankAccount().clabe("012680012570003085").holderName("mi nombre"));
            Assert.fail("Bank Account should exist.");
        } catch (OpenpayServiceException e) {
            Assert.assertEquals(409, e.getHttpCode().intValue());
        }
    }
}
