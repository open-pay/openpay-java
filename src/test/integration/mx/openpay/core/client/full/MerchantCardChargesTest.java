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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.core.requests.transactions.ConfirmCaptureParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.core.client.test.TestUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class MerchantCardChargesTest extends BaseTest {

    private Card registeredCard;

    @Before
    public void setUp() throws Exception {
        this.registeredCard = this.api.cards().create(new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(14)
                .address(TestUtils.prepareAddress()));
    }

    @After
    public void tearDown() throws Exception {
        this.api.cards().delete(this.registeredCard.getId());
    }

    @Test
    public void testCreate_Customer_WithId() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId));
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        Assert.assertNotNull(transaction.getFee());
    }

    @Test
    public void testCreate_Customer_WithCaptureFalse() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        BigDecimal initialBalance = this.api.merchant().get().getBalance();
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .capture(false));
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        assertEquals("in_progress", transaction.getStatus());

        BigDecimal newBalance = this.api.merchant().get().getBalance();
        assertTrue(newBalance.compareTo(initialBalance) == 0);
        Charge confirmed = this.api.charges().confirmCapture(new ConfirmCaptureParams().chargeId(transaction.getId())
                .amount(amount));
        newBalance = this.api.merchant().get().getBalance();
        assertTrue(initialBalance.add(amount).compareTo(newBalance) == 0);
        assertEquals("completed", confirmed.getStatus());
    }

    @Test
    public void testCreate_Customer_WithCaptureFalse_LessAmount() throws ServiceUnavailableException,
            OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        BigDecimal initialBalance = this.api.merchant().get().getBalance();
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        CreateCardChargeParams charge = new CreateCardChargeParams()
                .cardId(this.registeredCard.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .capture(false);
        Charge transaction = this.api.charges().create(charge);
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        assertEquals("in_progress", transaction.getStatus());

        BigDecimal newBalance = this.api.merchant().get().getBalance();
        assertTrue(initialBalance.compareTo(newBalance) == 0);

        BigDecimal confirmedAmount = BigDecimal.ONE;
        Charge confirmed = this.api.charges().confirmCapture(new ConfirmCaptureParams()
                .chargeId(transaction.getId()).amount(confirmedAmount));

        newBalance = this.api.merchant().get().getBalance();
        assertThat(newBalance, TestUtils.equalsAmount(initialBalance.add(confirmedAmount)));
        assertTrue(confirmedAmount.compareTo(confirmed.getAmount()) == 0);
        assertTrue(this.api.charges().get(transaction.getId()).getAmount()
                .compareTo(confirmedAmount) == 0);
        assertEquals("completed", confirmed.getStatus());
    }

    @Test
    public void testCreate_Customer_WithCard() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        Charge charge = this.api.charges().create(new CreateCardChargeParams()
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Juanito Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(14))
                .amount(amount)
                .description(desc));
        assertNotNull(charge);
        assertNotNull(charge.getCard());
        assertNull(charge.getCard().getCvv2());
        assertNull(charge.getCard().getId());
    }

    @Test
    public void testCreate_Customer_NoCardOrId() throws Exception {
        BigDecimal amount = new BigDecimal("10000.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        try {
            this.api.charges().create(new CreateCardChargeParams()
                    .amount(amount)
                    .description(desc)
                    .orderId(orderId));
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(400, e.getHttpCode().intValue());
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testRefund_Customer_Old() throws Exception {
        BigDecimal initialBalance = this.api.merchant().get().getBalance();
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        Charge transaction = this.api.charges().create(new CreateCardChargeParams()
                .amount(amount)
                .description(desc)
                .cardId(this.registeredCard.getId()));
        String originalTransactionId = transaction.getId();
        assertNotNull(transaction);
        assertNull(transaction.getRefund());

        String refDesc = "cancelacion (ignored description)";
        transaction = this.api.charges().refund(transaction.getId(), refDesc, null);
        assertNotNull(transaction.getRefund());
        assertEquals(refDesc, transaction.getRefund().getDescription());

        transaction = this.api.charges().get(originalTransactionId);
        assertNotNull(transaction.getRefund());
        assertTrue(this.api.merchant().get().getBalance().compareTo(initialBalance) == 0);
    }

    @Test
    public void testRefund_Customer() throws Exception {
        BigDecimal initialBalance = this.api.merchant().get().getBalance();
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(new CreateCardChargeParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .cardId(this.registeredCard.getId()));
        String originalTransactionId = transaction.getId();
        assertNotNull(transaction);
        assertNull(transaction.getRefund());
        String refDesc = "cancelacion (ignored description)";
        transaction = this.api.charges().refund(new RefundParams()
                .chargeId(transaction.getId())
                .description(refDesc));
        assertNotNull(transaction.getRefund());
        assertNull(transaction.getRefund().getFee());
        assertEquals(refDesc, transaction.getRefund().getDescription());

        transaction = this.api.charges().get(originalTransactionId);
        assertNotNull(transaction.getRefund());
        assertTrue(this.api.merchant().get().getBalance().compareTo(initialBalance) == 0);
    }

    @Test
    public void testGet_Customer() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId));
        Charge charge = this.api.charges().get(transaction.getId());
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        assertNotNull(charge);
    }

    @Test
    public void testListMerchantCardCharges_Empty() throws Exception {
        List<Charge> charges = this.api.charges().list(
                search().creationGte(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24))));
        assertTrue(charges.isEmpty());
    }

    @Test
    public void testListMerchantCardCharges() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description(desc));
        this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description(desc));
        this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description(desc));
        this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description(desc));
        this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description(desc));
        List<Charge> charges = this.api.charges().list(search().limit(5));
        assertEquals(5, charges.size());
        charges = this.api.charges().list(search().limit(3));
        assertEquals(3, charges.size());
    }

    @Test
    public void testListMerchantCardCharges_Amount() throws Exception {
        Date date = new Date();
        BigDecimal amount = new BigDecimal(String.format("%tI%<td.%<tM", date));
        this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description("desc"));
        this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(BigDecimal.TEN).description("desc"));
        this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(new BigDecimal("123.00")).description("desc"));
        this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description("desc"));
        this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description("desc"));
        List<Charge> charges = this.api.charges().list(search().amount(amount).creation(new Date()));
        for (Charge charge : charges) {
            assertTrue(amount.compareTo(charge.getAmount()) == 0);
        }
    }

    @Test
    public void testList_CustomerDoesNotExist() throws Exception {
        try {
            this.api.charges().list("blahblahblah", search().limit(2));
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

}
