/*
 * Copyright 2014 Opencard Inc.
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
package co.openpay.core.client.full;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import mx.openpay.client.BankAccount;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class MerchantBankAccountsTest extends BaseTest {

    @Test
    public void testList() throws ServiceUnavailableException, OpenpayServiceException {
        List<BankAccount> bankAccounts = this.api.bankAccounts().list(null);
        assertThat(bankAccounts.size(), is(greaterThan(0)));
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
    public void testGet() throws Exception {
        BankAccount bankAccount = this.api.bankAccounts().list(null).get(0);
        bankAccount = this.api.bankAccounts().get(bankAccount.getId());
        assertEquals("012XXXXXXXXXX24616", bankAccount.getClabe());
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

}
