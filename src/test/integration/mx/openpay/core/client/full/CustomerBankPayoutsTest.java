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

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.Payout;
import mx.openpay.client.core.requests.transactions.CreateBankPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.enums.PayoutMethod;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class CustomerBankPayoutsTest extends BaseTest {

    private Customer customer;

    private BankAccount bankAccount;

    @Before
    public void setUp() throws Exception {
        this.customer = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .amount(new BigDecimal("5"))
                .description("Cargo")
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Juanito Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(20)));

        this.bankAccount = this.api.bankAccounts().create(this.customer.getId(), new BankAccount()
                .clabe("012298026516924616").holderName("Mi nombre"));
    }

    @After
    public void tearDown() throws Exception {
        if (this.bankAccount != null) {
            this.api.bankAccounts().delete(this.customer.getId(), this.bankAccount.getId());
        }
        this.api.customers().delete(this.customer.getId());
    }

    @Test
    public void testListCustomerPayouts() throws ServiceUnavailableException, OpenpayServiceException {
        this.api.payouts().create(this.customer.getId(), new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(BigDecimal.ONE).description("desc 1"));
        this.api.payouts().create(this.customer.getId(), new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(BigDecimal.ONE).description("desc 2"));
        this.api.payouts().create(this.customer.getId(), new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(BigDecimal.ONE).description("desc 3"));
        List<Payout> transactions = this.api.payouts().list(this.customer.getId(), search());
        assertEquals(3, transactions.size());
    }

    @Test
    public void testListCustomerPayouts_Empty() throws ServiceUnavailableException, OpenpayServiceException {
        List<Payout> transactions = this.api.payouts().list(this.customer.getId(), search());
        assertEquals(0, transactions.size());
    }

    @Test
    public void testGetCustomerPayout() throws ServiceUnavailableException, OpenpayServiceException {
        Payout payout = this.api.payouts().create(this.customer.getId(), new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(BigDecimal.ONE).description("desc 1"));
        Payout transaction = this.api.payouts().get(this.customer.getId(), payout.getId());
        Assert.assertNotNull(transaction.getId());
        Assert.assertNotNull(transaction.getAmount());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateCustomerBankPayout_WithId_Old() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = BigDecimal.ONE;
        String desc = "Ganancias";
        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().createForCustomer(this.customer.getId(), PayoutMethod.BANK_ACCOUNT,
                this.bankAccount.getId(), amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertThat(transaction.getAmount(), comparesEqualTo(amount));
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(this.customer.getId(), transaction.getCustomerId());
    }

    @Test
    public void testCreateCustomerBankPayout_WithId() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = BigDecimal.ONE;
        String desc = "Ganancias";
        String orderId = String.valueOf(System.currentTimeMillis());
        CreateBankPayoutParams createPayout = new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId);
        Payout transaction = this.api.payouts().create(this.customer.getId(), createPayout);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertThat(transaction.getAmount(), comparesEqualTo(amount));
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(this.customer.getId(), transaction.getCustomerId());
        Assert.assertNull(transaction.getFee());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateCustomerBankPayout_WithBankAccount_Old() throws ServiceUnavailableException,
            OpenpayServiceException {
        BigDecimal amount = BigDecimal.ONE;
        String desc = "Ganancias";

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().createForCustomer(this.customer.getId(),
                new BankAccount()
                        .clabe("012298026516924616")
                        .holderName("Cuenta")
                , amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertThat(transaction.getAmount(), comparesEqualTo(amount));
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(this.customer.getId(), transaction.getCustomerId());
    }

    @Test
    public void testCreateCustomerBankPayout_WithBankAccount() throws ServiceUnavailableException,
            OpenpayServiceException {
        BigDecimal amount = BigDecimal.ONE;
        String desc = "Ganancias";

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().create(this.customer.getId(), new CreateBankPayoutParams()
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
        Assert.assertEquals(this.customer.getId(), transaction.getCustomerId());
    }
}
