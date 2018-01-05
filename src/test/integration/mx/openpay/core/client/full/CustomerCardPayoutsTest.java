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

import java.math.BigDecimal;

import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.Payout;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.CreateCardPayoutParams;
import mx.openpay.client.enums.PayoutMethod;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.core.client.test.TestUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class CustomerCardPayoutsTest extends BaseTest {

    private Customer customer;

    private Card card;

    @Before
    public void setUp() throws Exception {
        this.customer = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
        this.card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4111111111111111")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(20)
                .address(TestUtils.prepareAddress()));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .amount(new BigDecimal("5"))
                .description("Cargo")
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Juanito Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(20)));
    }

    @After
    public void tearDown() throws Exception {
        if (this.card != null) {
            this.api.cards().delete(this.customer.getId(), this.card.getId());
        }
        this.api.customers().delete(this.customer.getId());
    }

    @Ignore("Card payouts unavailable")
    @SuppressWarnings("deprecation")
    @Test
    public void testCreate_Customer_Card_Old() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        this.card.setBankCode("012");

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().createForCustomer(this.customer.getId(), new Card()
                .cardNumber("4111111111111111")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(20)
                .address(TestUtils.prepareAddress()), amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(this.customer.getId(), transaction.getCustomerId());
    }

    @Ignore("Card payouts unavailable")
    @SuppressWarnings("deprecation")
    @Test
    public void testCreateCustomerCardPayout_CardId_Old() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        this.card.setBankCode("012");

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().createForCustomer(this.customer.getId(), PayoutMethod.CARD,
                this.card.getId(), amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(this.customer.getId(), transaction.getCustomerId());
    }
    
    @Ignore("Card payouts unavailable")
    @Test
    public void testCreate_Customer_Card() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        this.card.bankCode("012");

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().create(this.customer.getId(), new CreateCardPayoutParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .card(new Card()
                        .cardNumber("4111111111111111")
                        .holderName("Juanito Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(20)
                        .address(TestUtils.prepareAddress())));
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(this.customer.getId(), transaction.getCustomerId());
        Assert.assertNull(transaction.getFee());
    }

    @Ignore("Card payouts unavailable")
    @Test
    public void testCreateCustomerCardPayout_CardId() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        this.card.bankCode("012");

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().create(this.customer.getId(), new CreateCardPayoutParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .cardId(this.card.getId()));
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(this.customer.getId(), transaction.getCustomerId());
    }
}
