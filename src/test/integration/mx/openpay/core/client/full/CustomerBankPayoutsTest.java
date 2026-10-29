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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import mx.openpay.client.BankAccount;
import mx.openpay.client.Customer;
import mx.openpay.client.Payout;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.BankAccountOperations;
import mx.openpay.client.core.operations.ChargeOperations;
import mx.openpay.client.core.operations.CustomerOperations;
import mx.openpay.client.core.operations.PayoutOperations;
import mx.openpay.client.core.requests.transactions.CreateBankPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.enums.PayoutMethod;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerBankPayoutsTest {

    @Mock
    private OpenpayAPI api;

    @Mock
    private CustomerOperations customerOperations;

    @Mock
    private ChargeOperations chargeOperations;

    @Mock
    private BankAccountOperations bankAccountOperations;

    @Mock
    private PayoutOperations payoutOperations;

    private Customer customer;
    private BankAccount bankAccount;

    @Before
    public void setUp() throws Exception {
        when(api.customers()).thenReturn(customerOperations);
        when(api.charges()).thenReturn(chargeOperations);
        when(api.bankAccounts()).thenReturn(bankAccountOperations);
        when(api.payouts()).thenReturn(payoutOperations);

        customer = new Customer();
        customer.setId("test-customer-id");
        when(customerOperations.create(any(Customer.class))).thenReturn(customer);

        bankAccount = new BankAccount();
        bankAccount.setId("test-bank-account-id");
        when(bankAccountOperations.create(eq(customer.getId()), any(BankAccount.class))).thenReturn(bankAccount);

        this.customer = this.api.customers().create(new Customer()
                .name("Jorge Perez").email("juan.perez@correo.com")
                .phoneNumber("44212033000")
                .requiresAccount(true));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .amount(new BigDecimal("5"))
                .description("Cargo"));

        this.bankAccount = this.api.bankAccounts().create(this.customer.getId(), new BankAccount()
                .clabe("012298026516924616").holderName("Mi nombre"));
    }

    @Test
    public void testListCustomerPayouts() throws ServiceUnavailableException, OpenpayServiceException {
        Payout payout1 = buildPayout(BigDecimal.ONE, "desc 1");
        Payout payout2 = buildPayout(BigDecimal.ONE, "desc 2");
        Payout payout3 = buildPayout(BigDecimal.ONE, "desc 3");

        when(payoutOperations.create(eq(this.customer.getId()), any(CreateBankPayoutParams.class)))
                .thenReturn(payout1).thenReturn(payout2).thenReturn(payout3);
        when(payoutOperations.list(eq(this.customer.getId()), any(SearchParams.class)))
                .thenReturn(Arrays.asList(payout1, payout2, payout3));

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
        when(payoutOperations.list(eq(this.customer.getId()), any(SearchParams.class)))
                .thenReturn(Collections.emptyList());

        List<Payout> transactions = this.api.payouts().list(this.customer.getId(), search());
        assertEquals(0, transactions.size());
    }

    @Test
    public void testGetCustomerPayout() throws ServiceUnavailableException, OpenpayServiceException {
        Payout createdPayout = buildPayout(BigDecimal.ONE, "desc 1");
        createdPayout.setId("payout-test-id");

        when(payoutOperations.create(eq(this.customer.getId()), any(CreateBankPayoutParams.class)))
                .thenReturn(createdPayout);
        when(payoutOperations.get(eq(this.customer.getId()), eq("payout-test-id")))
                .thenReturn(createdPayout);

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

        Payout expectedPayout = buildPayout(amount, desc);
        expectedPayout.setCustomerId(this.customer.getId());
        expectedPayout.setCreationDate(new Date());

        when(payoutOperations.createForCustomer(eq(this.customer.getId()), eq(PayoutMethod.BANK_ACCOUNT),
                eq(this.bankAccount.getId()), eq(amount), eq(desc), eq(orderId))).thenReturn(expectedPayout);

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

        Payout expectedPayout = buildPayout(amount, desc);
        expectedPayout.setCustomerId(this.customer.getId());
        expectedPayout.setCreationDate(new Date());

        when(payoutOperations.create(eq(this.customer.getId()), any(CreateBankPayoutParams.class)))
                .thenReturn(expectedPayout);

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

        Payout expectedPayout = buildPayout(amount, desc);
        expectedPayout.setCustomerId(this.customer.getId());
        expectedPayout.setCreationDate(new Date());

        when(payoutOperations.createForCustomer(eq(this.customer.getId()), any(BankAccount.class),
                eq(amount), eq(desc), eq(orderId))).thenReturn(expectedPayout);

        Payout transaction = this.api.payouts().createForCustomer(this.customer.getId(),
                new BankAccount()
                        .clabe("012298026516924616")
                        .holderName("Cuenta"),
                amount, desc, orderId);

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

        Payout expectedPayout = buildPayout(amount, desc);
        expectedPayout.setCustomerId(this.customer.getId());
        expectedPayout.setCreationDate(new Date());

        when(payoutOperations.create(eq(this.customer.getId()), any(CreateBankPayoutParams.class)))
                .thenReturn(expectedPayout);

        Payout transaction = this.api.payouts().create(this.customer.getId(), new CreateBankPayoutParams()
                .bankAccount(new BankAccount()
                        .clabe("012298026516924616")
                        .holderName("Cuenta"))
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .currency("MXN"));

        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertThat(transaction.getAmount(), comparesEqualTo(amount));
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(this.customer.getId(), transaction.getCustomerId());
    }

    private Payout buildPayout(final BigDecimal amount, final String description) {
        Payout payout = new Payout();
        payout.setAmount(amount);
        payout.setDescription(description);
        return payout;
    }
}
