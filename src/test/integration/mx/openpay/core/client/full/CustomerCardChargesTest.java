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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.core.requests.transactions.ConfirmCaptureParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.enums.Currency;
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
@Slf4j
public class CustomerCardChargesTest extends BaseTest {

    private Customer customer;

    private Customer customerNoAccount;

    private Card registeredCard;

    private Card registeredCardNoAccount;

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
                .expirationYear(20)
                .address(TestUtils.prepareAddress()));
        this.customerNoAccount = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013").requiresAccount(false));
        this.registeredCardNoAccount = this.api.cards().create(this.customerNoAccount.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(20)
                .address(TestUtils.prepareAddress()));
    }

    @After
    public void tearDown() throws Exception {
        if (this.registeredCard != null) {
            this.api.cards().delete(this.customer.getId(), this.registeredCard.getId());
        }
        if (this.registeredCardNoAccount != null) {
            this.api.cards().delete(this.customerNoAccount.getId(), this.registeredCardNoAccount.getId());
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
        String transactionId = transaction.getId();
        transaction = this.api.charges().get(this.customer.getId(), transactionId);
        assertThat(transaction.getId(), is(transactionId));
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        Assert.assertNotNull(transaction.getFee());
        // No se puede obtener tambien desde el merchant
        try {
            this.api.charges().get(transactionId);
            fail("Expected can't find");
        } catch (OpenpayServiceException e) {
            assertThat(e.getHttpCode(), is(404));
        }
    }

    @Test
    public void testCreate_Customer_WithId_WithoutAccount() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(this.customerNoAccount.getId(), new CreateCardChargeParams()
                .cardId(this.registeredCardNoAccount.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId));
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        String transactionId = transaction.getId();
        transaction = this.api.charges().get(this.customerNoAccount.getId(), transactionId);
        assertThat(transaction.getId(), is(transactionId));
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        // Se puede obtener tambien desde el merchant
        transaction = this.api.charges().get(transactionId);
        assertThat(transaction.getId(), is(transactionId));
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        this.api.charges().refund(new RefundParams().chargeId(transactionId));
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
    public void testCreate_Customer_WithCaptureFalse_WithoutAccount() throws ServiceUnavailableException,
            OpenpayServiceException {
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
                        .expirationYear(20))
                .amount(amount)
                .description(desc));
        assertNotNull(charge);
        assertNotNull(charge.getCard());
        assertNull(charge.getCard().getCvv2());
        assertNull(charge.getCard().getId());
    }

	@Test
	public void testCreate_Customer_WithCard_currencyUSD() throws Exception {
		BigDecimal amount = new BigDecimal("1.00");
		String desc = "Pago de taxi";
		Map<String, String> metadata = new LinkedHashMap<String, String>();
		metadata.put("origin", "Mexico");
		metadata.put("destination", "Puebla");
		metadata.put("seats", "3");
		Charge charge = this.api.charges().create(
				this.customer.getId(),
				new CreateCardChargeParams()
						.card(new Card().cardNumber("4111111111111111").holderName("Juanito Pérez Nuñez").cvv2("111")
								.expirationMonth(9).expirationYear(20)).amount(amount).description(desc)
						.currency(Currency.USD).metadata(metadata).deviceSessionId("Tu2yXO0sJpT6KUVi1g4IWDOEmIHP69XI"));
		assertNotNull(charge);
		assertNotNull(charge.getCard());
		assertNull(charge.getCard().getCvv2());
		assertNull(charge.getCard().getId());
		assertNotNull(charge.getExchangeRate());
		assertNotNull(charge.getExchangeRate().getValue());
		assertNotNull(charge.getMetadata());
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

    @Test
    public void testCreate_Customer_SingleCharge() throws Exception {
        BigDecimal amount = new BigDecimal("100.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Customer customer = new Customer().name("Karina Solis").email("o@mail.com").phoneNumber("12345678");
        Charge charge = this.api.charges().create(new CreateCardChargeParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .cardId(this.registeredCard.getId())
                .customer(customer));
        assertNotNull(charge);
        assertNotNull(charge.getCard());
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
    public void testRefund_Customer_NoAccount() throws Exception {
        BigDecimal balanceOld = this.api.merchant().get().getBalance();
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(this.customerNoAccount.getId(), new CreateCardChargeParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .cardId(this.registeredCardNoAccount.getId()));
        String originalTransactionId = transaction.getId();
        assertNotNull(transaction);
        assertNull(transaction.getRefund());
        String refDesc = "cancelacion (ignored description)";
        transaction = this.api.charges().refund(this.customerNoAccount.getId(), new RefundParams()
                .chargeId(transaction.getId())
                .description(refDesc));
        assertNotNull(transaction.getRefund());
        assertEquals(refDesc, transaction.getRefund().getDescription());

        transaction = this.api.charges().get(this.customerNoAccount.getId(), originalTransactionId);
        assertNotNull(transaction.getRefund());
        BigDecimal balanceNew = this.api.merchant().get().getBalance();
        assertThat(balanceNew, comparesEqualTo(balanceOld));
    }

    @Test
    public void testRefund_Customer_NoAccount_FromMerchant() throws Exception {
        BigDecimal balanceOld = this.api.merchant().get().getBalance();
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(this.customerNoAccount.getId(), new CreateCardChargeParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .cardId(this.registeredCardNoAccount.getId()));
        String originalTransactionId = transaction.getId();
        assertNotNull(transaction);
        assertNull(transaction.getRefund());
        String refDesc = "cancelacion (ignored description)";
        transaction = this.api.charges().refund(new RefundParams()
                .chargeId(transaction.getId())
                .description(refDesc));
        assertNotNull(transaction.getRefund());
        assertEquals(refDesc, transaction.getRefund().getDescription());

        transaction = this.api.charges().get(this.customerNoAccount.getId(), originalTransactionId);
        assertNotNull(transaction.getRefund());
        transaction = this.api.charges().get(originalTransactionId);
        assertNotNull(transaction.getRefund());
        BigDecimal balanceNew = this.api.merchant().get().getBalance();
        assertThat(balanceNew, comparesEqualTo(balanceOld));
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
