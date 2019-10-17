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
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.Address;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.GatewayParams;
import mx.openpay.client.HttpContext;
import mx.openpay.client.ShipTo;
import mx.openpay.client.SimpleRefund;
import mx.openpay.client.core.requests.transactions.ConfirmCaptureParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.enums.Currency;
import mx.openpay.client.enums.UseCardPointsType;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;
import mx.openpay.core.client.test.TestUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Slf4j
@Ignore
public class MerchantCardChargesTest extends BaseTest {

    private Card registeredCard;

    @Before
    public void setUp() throws Exception {
        this.registeredCard = this.api.cards().create(new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(20)
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
                .orderId(orderId).cvv2("235")
                .gateway(new GatewayParams()
                        .addData("amex", "keyName", "data")
                        .addData("amex", "otherKey", "value")));

        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        assertThat(transaction.getCardPoints(), is(nullValue()));
        Assert.assertNotNull(transaction.getFee());
    }

    @Test
    public void testSearchOrderId() throws ServiceUnavailableException, OpenpayServiceException {
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
        assertThat(transaction.getCardPoints(), is(nullValue()));
        Assert.assertNotNull(transaction.getFee());
        List<Charge> list = api.charges().list(new SearchParams().orderId(orderId));
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getId(), is(transaction.getId()));
    }

    @Test
    public void testCreate_Customer_WithPoints_Small() throws ServiceUnavailableException, OpenpayServiceException {
        assertThat(this.registeredCard.isPointsCard(), is(true));
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .useCardPoints(UseCardPointsType.MIXED));
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        // TODO se comenta por que en ambiente de desarrollo regresa cardPoints nulo.
        // assertThat(transaction.getCardPoints(), is(notNullValue()));
        // assertThat(transaction.getCardPoints().getUsed(), is(greaterThan(BigDecimal.ZERO)));
        // assertThat(transaction.getCardPoints().getRemaining(), is(greaterThan(BigDecimal.ZERO)));
        // assertThat(transaction.getCardPoints().getAmount(), comparesEqualTo(amount));
        Assert.assertNotNull(transaction.getFee());
    }

    @Test
    public void testCreate_Customer_WithPoints_Big() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("30.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(new CreateCardChargeParams()
                .cardId(this.registeredCard.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .useCardPoints(UseCardPointsType.MIXED));
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        // TODO se comenta por que en ambiente de desarrollo regresa cardPoints nulo.
        // assertThat(transaction.getCardPoints(), is(notNullValue()));
        // assertThat(transaction.getCardPoints().getUsed(), is(greaterThan(BigDecimal.ZERO)));
        // assertThat(transaction.getCardPoints().getRemaining(), is(greaterThan(BigDecimal.ZERO)));
        // assertThat(transaction.getCardPoints().getAmount(), comparesEqualTo(new BigDecimal("22.5")));
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
    public void testCreate_Charge_WithCard() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        Charge charge = this.api.charges().create(new CreateCardChargeParams()
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Juanito Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(20))
                .amount(amount)
                .description(desc)
                .shipTo(new ShipTo()
                        .firstName("Juanito")
                        .lastName("Perez")
                        .email("juanitoperez@example.com")
                        .phoneNumber("0000000000")
                        .method("shipping method")
                        .address(new Address()
                                .line1("line1")
                                .line2("line2")
                                .line3("")
                                .city("city")
                                .countryCode("MX")
                                .postalCode("76000")
                                .state("state")))
                .httpContext(new HttpContext()
                        .ip("127.0.0.1")
                        .browser("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
                        .domain("www.example.com")));
        assertNotNull(charge);
        assertNotNull(charge.getCard());
        assertNull(charge.getCard().getCvv2());
        assertNull(charge.getCard().getId());
        assertThat(charge.getRiskData().getScore(), is(notNullValue()));
        assertThat(charge.getRiskData().getRules(), is(not(empty())));
    }

    @Test
    public void testCreate_Charge_WithCard_PhoneOrder() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        Charge charge = this.api.charges().create(new CreateCardChargeParams()
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Juanito Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(20))
                .amount(amount)
                .description(desc)
                .isPhoneOrder(true));
        assertNotNull(charge);
        assertNotNull(charge.getCard());
        assertNull(charge.getCard().getCvv2());
        assertNull(charge.getCard().getId());
    }

    @Test
    public void testCreate_Charge_WithCard_3dSecure() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        Charge charge = this.api.charges().create(new CreateCardChargeParams()
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Juanito Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(20))
                .amount(amount)
                .description(desc)
                .use3dSecure(true)
                .redirectUrl("http://openpay.mx"));
        assertNotNull(charge);
        assertNotNull(charge.getCard());
        assertNotNull(charge.getPaymentMethod().getUrl());
    }
    
    @Test
    public void testCreate_Charge_WithCard_currencyUSD_metadata() throws Exception {
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Pago de taxi";
        Map<String, String> metadata = new LinkedHashMap<String, String>();
        metadata.put("origin", "Mexico");
        metadata.put("destination", "Puebla");
        metadata.put("seats", "3");
        Charge charge = this.api.charges().create(
                new CreateCardChargeParams()
                        .card(new Card().cardNumber("4111111111111111").holderName("Juanito Pérez Nuñez").cvv2("110")
                                .expirationMonth(12).expirationYear(20))
                        .amount(amount).description(desc)
                        .currency(Currency.USD).metadata(metadata).deviceSessionId("Tu2yXO0sJpT6KUVi1g4IWDOEmIHP69XI"));
        assertNotNull(charge);
        assertNotNull(charge.getCard());
        assertNull(charge.getCard().getCvv2());
        assertNull(charge.getCard().getId());
        assertNotNull(charge.getMetadata());
        assertNotNull(charge.getExchangeRate());
        assertNotNull(charge.getExchangeRate().getValue());
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

    //@Test
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
                .description(refDesc)
                .gateway(new GatewayParams()
                        .addData("amex_iso", "key123", "value456")
                        .addData("amex_iso", "abc123", "def456")));
        assertNotNull(transaction.getRefund());
        assertNull(transaction.getRefund().getFee());
        assertEquals(refDesc, transaction.getRefund().getDescription());

        transaction = this.api.charges().get(originalTransactionId);
        assertNotNull(transaction.getRefund());
        assertTrue(this.api.merchant().get().getBalance().compareTo(initialBalance) == 0);
        assertThat(transaction.getRefunds().size(), is(1));
        SimpleRefund refund = transaction.getRefunds().get(0);
        assertThat(refund.getId(), is(notNullValue()));
        assertThat(refund.getStatus(), is("completed"));
        assertThat(refund.getOperationDate(), is(notNullValue()));
        assertThat(refund.getAmount(), comparesEqualTo(amount));
        
    }

    //@Test
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

    //@Test
    public void testListMerchantCardCharges_Empty() throws Exception {
        List<Charge> charges = this.api.charges().list(
                search().creationGte(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24))));
        assertTrue(charges.isEmpty());
    }

    //@Test
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

    //@Test
    public void testListMerchantCardCharges_Amount() throws Exception {
        Date date = new Date();
        BigDecimal amount = new BigDecimal(String.format("%tI%<td.%<tM", date)).remainder(new BigDecimal("1000"));
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

    //@Test
    public void testList_CustomerDoesNotExist() throws Exception {
        try {
            this.api.charges().list("blahblahblah", search().limit(2));
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    //@Test
    //@Ignore
    public void testFailedRiskTransaction() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        try {
            this.api.charges().create(new CreateCardChargeParams()
                    .card(new Card()
                            .cardNumber("4444444444444448")
                            .holderName("Juanito Pérez Nuñez")
                            .cvv2("111")
                            .expirationMonth(9)
                            .expirationYear(20))
                    .customer(new Customer()
                            .name("Juanito Pérez Nuñez")
                            .email("ERROR.FRAUD@OPENPAY.MX"))
                    .amount(amount)
                    .description(desc));
        } catch (OpenpayServiceException e) {
            assertEquals(402, e.getHttpCode().intValue());
            assertThat(e.getErrorCode(), is(3003));
            assertThat(e.getRiskData().getScore(), is(notNullValue()));
            assertThat(e.getRiskData().getRules(), is(not(empty())));
        }
    }

}
