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

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import co.openpay.core.client.test.TestUtils;
import mx.openpay.client.Card;
import mx.openpay.client.Payout;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.CreateCardPayoutParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class MerchantCardPayoutsTest extends BaseTest {

    private Card card;

    @Before
    public void setUp() throws Exception {
        this.card = this.api.cards().create(new Card()
                .cardNumber("4111111111111111")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(20)
                .address(TestUtils.prepareAddress()));
        this.api.charges().create(new CreateCardChargeParams()
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
        this.api.cards().delete(this.card.getId());
    }

    @Ignore("Card payouts unavailable")
    @SuppressWarnings("deprecation")
    @Test
    public void testCreate_Merchant_Card_Old() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        this.card.setBankCode("012");

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().createForMerchant(new Card()
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
        Assert.assertNull(transaction.getCustomerId());
    }

    @Test
    @Ignore("Card payouts unavailable")
    public void testCreate_Merchant_Card() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        this.card.bankCode("012");

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().create(new CreateCardPayoutParams()
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
        Assert.assertNull(transaction.getCustomerId());
        Assert.assertNull(transaction.getFee());
    }

    @Test
    @Ignore("Card payouts unavailable")
    public void testCreateMerchantCardPayout_CardId() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        this.card.bankCode("012");

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().create(new CreateCardPayoutParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .cardId(this.card.getId()));
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertNull(transaction.getCustomerId());
    }
}
