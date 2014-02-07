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

import static mx.openpay.client.utils.SearchParams.search;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;

import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.core.requests.transactions.ConfirmCaptureParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.core.client.test.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class CustomerCardChargesTest extends BaseTest {

    private Customer customer;

    private Card registeredCard;

    @Before
    public void setUp() throws Exception {
        this.customer = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
        this.registeredCard = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(14)
                .address(TestUtils.prepareAddress()));
    }

    @After
    public void tearDown() throws Exception {
        if (this.registeredCard != null) {
            this.api.cards().delete(this.customer.getId(), this.registeredCard.getId());
        }
        this.api.customers().delete(this.customer.getId());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreate_Customer_WithId_Old() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(this.customer.getId(), this.registeredCard.getId(), amount,
                desc, orderId);
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
    }

    @Test
    public void testCreate_Customer_WithId() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId));
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
    }

    @Test
    public void testCreate_Customer_WithCaptureFalse() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .capture(false));
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        assertEquals("in_progress", transaction.getStatus());

        BigDecimal newBalance = this.api.customers().get(this.customer.getId()).getBalance();
        assertTrue(newBalance.compareTo(BigDecimal.ZERO) == 0);
        Charge confirmed = this.api.charges().confirmCapture(this.customer.getId(), new ConfirmCaptureParams()
                .chargeId(transaction.getId())
                .amount(amount));
        newBalance = this.api.customers().get(this.customer.getId()).getBalance();
        assertTrue(amount.compareTo(newBalance) == 0);
        assertEquals("completed", confirmed.getStatus());
    }

    @Test
    public void testCreate_Customer_WithCaptureFalse_LessAmount() throws ServiceUnavailableException,
            OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        CreateCardChargeParams charge = new CreateCardChargeParams()
                .cardId(this.registeredCard.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .capture(false);
        Charge transaction = this.api.charges().create(this.customer.getId(), charge);
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        assertEquals("in_progress", transaction.getStatus());

        BigDecimal newBalance = this.api.customers().get(this.customer.getId()).getBalance();
        assertTrue(BigDecimal.ZERO.compareTo(newBalance) == 0);

        BigDecimal confirmedAmount = BigDecimal.ONE;
        Charge confirmed = this.api.charges().confirmCapture(this.customer.getId(), new ConfirmCaptureParams()
                .chargeId(transaction.getId())
                .amount(confirmedAmount));

        newBalance = this.api.customers().get(this.customer.getId()).getBalance();
        assertThat(newBalance, TestUtils.equalsAmount(confirmedAmount));
        assertTrue(confirmedAmount.compareTo(confirmed.getAmount()) == 0);
        assertTrue(this.api.charges().get(this.customer.getId(), transaction.getId()).getAmount()
                .compareTo(confirmedAmount) == 0);
        assertEquals("completed", confirmed.getStatus());
    }

    @Test
    public void testCreate_Customer_WithCard() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        Charge charge = this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
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
            this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
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
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        Charge transaction = this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .amount(amount)
                .description(desc)
                .cardId(this.registeredCard.getId()));
        String originalTransactionId = transaction.getId();
        assertNotNull(transaction);
        assertNull(transaction.getRefund());

        String refDesc = "cancelacion (ignored description)";
        transaction = this.api.charges().refund(this.customer.getId(), transaction.getId(), refDesc, null);
        assertNotNull(transaction.getRefund());
        assertEquals(refDesc, transaction.getRefund().getDescription());

        transaction = this.api.charges().get(this.customer.getId(), originalTransactionId);
        assertNotNull(transaction.getRefund());
        assertTrue(this.api.customers().get(this.customer.getId()).getBalance().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    public void testRefund_Customer() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .cardId(this.registeredCard.getId()));
        String originalTransactionId = transaction.getId();
        assertNotNull(transaction);
        assertNull(transaction.getRefund());
        String refDesc = "cancelacion (ignored description)";
        transaction = this.api.charges().refund(this.customer.getId(), new RefundParams()
                .chargeId(transaction.getId())
                .description(refDesc));
        assertNotNull(transaction.getRefund());
        assertEquals(refDesc, transaction.getRefund().getDescription());

        transaction = this.api.charges().get(this.customer.getId(), originalTransactionId);
        assertNotNull(transaction.getRefund());
        assertTrue(this.api.customers().get(this.customer.getId()).getBalance().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    public void testGet_Customer() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId));
        Charge charge = this.api.charges().get(this.customer.getId(), transaction.getId());
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        assertNotNull(charge);
    }

    @Test
    public void testListCustomerCardCharges_Empty() throws Exception {
        List<Charge> charges = this.api.charges().list(this.customer.getId(), null);
        assertTrue(charges.isEmpty());
    }

    @Test
    public void testListCustomerCardCharges() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description(desc));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description(desc));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description(desc));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description(desc));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description(desc));
        List<Charge> charges = this.api.charges().list(this.customer.getId(), null);
        assertEquals(5, charges.size());
        charges = this.api.charges().list(this.customer.getId(), search().limit(3));
        assertEquals(3, charges.size());
    }

    @Test
    public void testList_Customer_Amount() throws Exception {
        BigDecimal amount = BigDecimal.ONE;
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description("desc"));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(BigDecimal.TEN).description("desc"));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(new BigDecimal("123.00")).description("desc"));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description("desc"));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(amount).description("desc"));
        List<Charge> charges = this.api.charges().list(this.customer.getId(), search().amount(amount));
        assertThat(charges.size(), is(3));
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

    @Test
    public void testGetCustomerCharge_FromMerchant() throws Exception {
        Charge charge = this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCard.getId()).amount(BigDecimal.TEN).description("desc"));
        try {
            this.api.charges().get(charge.getId());
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

}
