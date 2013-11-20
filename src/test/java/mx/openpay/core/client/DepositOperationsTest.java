/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.CUSTOMER_ID;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import mx.openpay.client.Address;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.ChargeOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
public class DepositOperationsTest {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    OpenpayAPI api;

    ChargeOperations ops;

    @Before
    public void setUp() throws Exception {
        this.api = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID);
        this.ops = this.api.charges();
    }

    @Test
    public void testCreate_Customer_WithId() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10000.00");
        String desc = "Pago de taxi";

        List<Card> cards = this.api.cards().list(CUSTOMER_ID, search().offset(0).limit(10));
        Assert.assertNotNull(cards);

        String orderId = this.dateFormat.format(new Date());
        Charge transaction = this.ops.create(CUSTOMER_ID, cards.get(0).getId(), amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
    }

    @Test
    public void testCreate_Customer_WithCard() throws Exception {
        Address address = this.createAddress();

        Card card = new Card();
        card.setCardNumber("5243385358972033");
        card.setHolderName("Juanito Pérez Nuñez");
        card.setCvv2("111");
        card.setExpirationMonth("09");
        card.setExpirationYear("14");
        card.setAddress(address);

        BigDecimal amount = new BigDecimal("10000.00");
        String desc = "Pago de taxi";
        String orderId = this.dateFormat.format(new Date());
        Charge deposit = this.ops.create(CUSTOMER_ID, card, amount, desc, orderId);
        assertNotNull(deposit);
        assertNotNull(deposit.getCard());
        assertNull(deposit.getCard().getCvv2());
        assertNull(deposit.getCard().getId());
    }

    @Test
    public void testCreate_Customer_NoCard() throws Exception {
        BigDecimal amount = new BigDecimal("10000.00");
        String desc = "Pago de taxi";
        String orderId = this.dateFormat.format(new Date());
        try {
            this.ops.create(CUSTOMER_ID, (Card) null, amount, desc, orderId);
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(422, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testCreate_Customer_NoId() throws Exception {
        BigDecimal amount = new BigDecimal("10000.00");
        String desc = "Pago de taxi";
        String orderId = this.dateFormat.format(new Date());
        try {
            this.ops.create(CUSTOMER_ID, (String) null, amount, desc, orderId);
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(422, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testRefund_Customer() throws Exception {
        BigDecimal amount = new BigDecimal("10000.00");
        String desc = "Pago de taxi";
        List<Card> cards = this.api.cards().list(CUSTOMER_ID, search().offset(0).limit(10));
        Assert.assertNotNull(cards);
        String orderId = this.dateFormat.format(new Date());

        Charge transaction = this.ops.create(CUSTOMER_ID, cards.get(0).getId(), amount, desc, orderId);
        String originalTransactionId = transaction.getId();
        Assert.assertNotNull(transaction);
        assertNull(transaction.getRefund());

        transaction = this.ops.refund(CUSTOMER_ID, transaction.getId(), "cancelacion", null);
        Assert.assertNotNull(transaction.getRefund());
        Assert.assertEquals("cancelacion", transaction.getRefund().getDescription());

        transaction = this.ops.get(CUSTOMER_ID, originalTransactionId);
        assertNotNull(transaction.getRefund());
    }

    @Test
    public void testGet_Customer() throws Exception {
        Charge deposit = this.ops.get(CUSTOMER_ID, "t2eqcqtb1uq2k746eiti");
        assertNotNull(deposit);
    }

    @Test
    public void testList_Customer() throws Exception {
        List<Charge> deposits = this.ops.list(CUSTOMER_ID, search().limit(3));
        assertEquals(3, deposits.size());

        deposits = this.ops.list(CUSTOMER_ID, search().limit(5));
        assertEquals(5, deposits.size());
    }

    @Test
    public void testList_Customer_Empty() throws Exception {
        List<Charge> deposits = this.ops.list(CUSTOMER_ID, search().limit(2).offset(10000));
        assertTrue(deposits.isEmpty());
    }

    @Test
    public void testList_CustomerDoesNotExist() throws Exception {
        try {
            this.ops.list("blahblahblah", search().limit(2));
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testCreate_Merchant_WithCard() throws Exception {
        Address address = this.createAddress();

        Card card = new Card();
        card.setCardNumber("5243385358972033");
        card.setHolderName("Juanito Pérez Nuñez");
        card.setCvv2("111");
        card.setExpirationMonth("09");
        card.setExpirationYear("14");
        card.setAddress(address);

        BigDecimal amount = new BigDecimal("10000.00");
        String desc = "Pago de taxi";
        String orderId = this.dateFormat.format(new Date());
        Charge deposit = this.ops.create(card, amount, desc, orderId);
        assertNotNull(deposit);
        assertNotNull(deposit.getCard());
        assertNull(deposit.getCard().getCvv2());
        assertNull(deposit.getCard().getId());
    }

    private Address createAddress() {
        Address address = new Address();
        address.setCity("Querétaro");
        address.setLine1("Camino #11 - 01");
        address.setPostalCode("76090");
        address.setState("Queretaro");
        address.setCountryCode("MX");
        return address;
    }

    @Test
    public void testCreate_Merchant_NoCard() throws Exception {
        BigDecimal amount = new BigDecimal("10000.00");
        String desc = "Pago de taxi";
        String orderId = this.dateFormat.format(new Date());
        try {
            this.ops.create(null, amount, desc, orderId);
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(400, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testRefund_Merchant() throws Exception {
        BigDecimal amount = new BigDecimal("10000.00");
        String desc = "Pago de taxi";
        String orderId = this.dateFormat.format(new Date());

        Charge transaction = this.ops.create(this.getCard(), amount, desc, orderId);
        String originalTransactionId = transaction.getId();
        Assert.assertNotNull(transaction);
        assertNull(transaction.getRefund());

        transaction = this.ops.refund(transaction.getId(), "cancelacion", null);
        Assert.assertNotNull(transaction.getRefund());
        Assert.assertEquals("cancelacion", transaction.getRefund().getDescription());

        transaction = this.ops.get(originalTransactionId);
        assertNotNull(transaction.getRefund());
    }

    @Test
    public void testGet_Merchant() throws Exception {
        Charge sale = this.ops.get("txvdd6sjzcrvbiragy66");
        assertNotNull(sale);
    }

    @Test
    public void testGet_MerchantForCustomer() throws Exception {
        Charge sale = this.ops.get("t2eqcqtb1uq2k746eiti");
        assertNotNull(sale);
    }

    @Test
    public void testList_Merchant() throws Exception {
        List<Charge> sale = this.ops.list(search().limit(3));
        assertEquals(3, sale.size());

        sale = this.ops.list(search().limit(5));
        assertEquals(5, sale.size());
    }

    private Card getCard() {
        Address address = this.createAddress();

        Card card = new Card();
        card.setCardNumber("5243385358972033");
        card.setHolderName("Holder");
        card.setExpirationMonth("12");
        card.setExpirationYear("15");
        card.setCvv2("123");
        card.setAddress(address);
        return card;
    }
}
