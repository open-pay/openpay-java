/*
 * Copyright 2013 Opencard Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.CUSTOMER_BANK_ACCOUNT;
import static mx.openpay.core.client.TestConstans.CUSTOMER_ID;
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
        String customerId = CUSTOMER_ID;
        BankAccount bank = this.bankAccountOps.create(customerId, "012298026516924616", "Mi nombre", null);
        Assert.assertNotNull(bank);
        this.bankAccountOps.delete(customerId, bank.getId());
    }

    @Test
    public void testDelete_DoesNotExist() throws Exception {
        try {
            this.bankAccountOps.delete(CUSTOMER_ID, "fesf4fcsdf");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testList() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = CUSTOMER_ID;
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
        String customerId = CUSTOMER_ID;
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
        BankAccount bankAccount = this.bankAccountOps.get(CUSTOMER_ID, CUSTOMER_BANK_ACCOUNT);
        assertNotNull(bankAccount);
        assertEquals("012680026840227260", bankAccount.getClabe());
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
        String customerId = CUSTOMER_ID;
        try {
            this.bankAccountOps.create(customerId, "012680012570003085", "mi nombre", null);
            Assert.fail("Bank Account should be exists.");
        } catch (OpenpayServiceException e) {
            Assert.assertEquals(409, e.getHttpCode().intValue());
            String bankId = CUSTOMER_BANK_ACCOUNT;
            BankAccount account = this.bankAccountOps.get(customerId, bankId);
            Assert.assertNotNull(account);
            Assert.assertNotNull(account.getId());
            Assert.assertNotNull(account.getBankName());
            Assert.assertEquals("BANCOMER", account.getBankName());
        }
    }

}
