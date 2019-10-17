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
package mx.openpay.core.client.full;

import static mx.openpay.client.utils.SearchParams.search;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Payout;
import mx.openpay.client.core.requests.transactions.CreateBankPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class MerchantBankPayoutsTest extends BaseTest {

    private BankAccount bankAccount;

    @Before
    public void setUp() throws Exception {
        this.api.charges().create(new CreateCardChargeParams()
                .amount(new BigDecimal("5"))
                .description("Cargo")
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Juanito Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(20)));

        this.bankAccount = this.api.bankAccounts().list(null).get(0);
    }

    @After
    public void tearDown() throws Exception {
    }

    
    /*public void testListMerchantPayouts() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal(new Random().nextInt(1000)).add(new BigDecimal("0.01"));
        this.api.payouts().create(new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(amount).description("desc 1"));
        this.api.payouts().create(new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(amount).description("desc 2"));
        this.api.payouts().create(new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(amount).description("desc 3"));
        List<Payout> transactions = this.api.payouts().list(search().amount(amount));
        assertEquals(3, transactions.size());
    }*/

    @Test
    public void testListMerchantPayouts_Empty() throws ServiceUnavailableException, OpenpayServiceException {
        List<Payout> transactions = this.api.payouts().list(
                search().creation(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24))));
        assertEquals(0, transactions.size());
    }

    @Test
    public void testGetMerchantPayout() throws ServiceUnavailableException, OpenpayServiceException {
        Payout payout = this.api.payouts().create(new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(BigDecimal.ONE).description("desc 1"));
        Payout transaction = this.api.payouts().get(payout.getId());
        Assert.assertNotNull(transaction.getId());
        Assert.assertNotNull(transaction.getAmount());
        Assert.assertNull(transaction.getFee());
    }

    @Test
    public void testCreateMerchantBankPayout_WithId() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = BigDecimal.ONE;
        String desc = "Ganancias";
        String orderId = String.valueOf(System.currentTimeMillis());
        CreateBankPayoutParams createPayout = new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId);
        Payout transaction = this.api.payouts().create(createPayout);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertThat(transaction.getAmount(), comparesEqualTo(amount));
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertNull(transaction.getCustomerId());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateMerchantBankPayout_WithBankAccount_Old() throws ServiceUnavailableException,
            OpenpayServiceException {
        BigDecimal amount = BigDecimal.ONE;
        String desc = "Ganancias";

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().createForMerchant(
                new BankAccount()
                        .clabe("012298026516924616")
                        .holderName("Cuenta")
                , amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertThat(transaction.getAmount(), comparesEqualTo(amount));
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertNull(transaction.getCustomerId());
    }

    @Test
    public void testCreateMerchantBankPayout_WithBankAccount() throws ServiceUnavailableException,
            OpenpayServiceException {
        BigDecimal amount = BigDecimal.ONE;
        String desc = "Ganancias";

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().create(new CreateBankPayoutParams()
                .bankAccount(new BankAccount()
                        .clabe("012298026516924616")
                        .holderName("Cuenta"))
                .amount(amount)
                .description(desc)
                .orderId(orderId));
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertThat(transaction.getAmount(), comparesEqualTo(amount));
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertNull(transaction.getCustomerId());
        Assert.assertNull(transaction.getFee());
    }

    @Test
    public void testCreateMerchantBankPayout_WithBankAccount_Breakdown() throws ServiceUnavailableException,
            OpenpayServiceException {
        BigDecimal amount = BigDecimal.ONE;
        String desc = "Ganancias";

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().create(new CreateBankPayoutParams()
                .bankAccount(new BankAccount()
                        .clabe("012298026516924616")
                        .holderName("Cuenta"))
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .makeBreakdown(true));
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertThat(transaction.getAmount(), comparesEqualTo(amount));
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals("payout", transaction.getTransactionType());
        Assert.assertNull(transaction.getCustomerId());
        Assert.assertNull(transaction.getFee());
    }
}
