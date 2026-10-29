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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.PaymentMethod;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.ChargeOperations;
import mx.openpay.client.core.operations.CustomerOperations;
import mx.openpay.client.core.requests.transactions.CreateStoreChargeParams;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerStoreChargesTest {

    @Mock
    private OpenpayAPI api;

    @Mock
    private ChargeOperations chargeOperations;

    @Mock
    private CustomerOperations customerOperations;

    private Customer customer;

    @Before
    public void setUp() throws Exception {
        when(api.customers()).thenReturn(customerOperations);
        when(api.charges()).thenReturn(chargeOperations);

        customer = new Customer();
        customer.setId("test-customer-id");
        when(customerOperations.create(any(Customer.class))).thenReturn(customer);

        this.customer = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .requiresAccount(true)
                .phoneNumber("44200000000"));
    }

    @Test
    public void testCreate_Customer_Store() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setReference("REF-STORE-001");
        paymentMethod.setBarcodeUrl("https://example.com/barcode.png");

        Charge expectedCharge = new Charge();
        expectedCharge.setStatus("in_progress");
        expectedCharge.setPaymentMethod(paymentMethod);

        when(chargeOperations.create(eq(this.customer.getId()), any(CreateStoreChargeParams.class)))
                .thenReturn(expectedCharge);

        Charge transaction = this.api.charges().create(this.customer.getId(),
                new CreateStoreChargeParams().amount(amount).description(desc).orderId(orderId));

        assertNotNull(transaction);
        assertNotNull(transaction.getPaymentMethod().getReference());
        assertNotNull(transaction.getPaymentMethod().getBarcodeUrl());
        assertThat(transaction.getStatus(), is("in_progress"));
        Assert.assertNull(transaction.getFee());
    }

    @Test
    public void testCreate_SingleCustomer_Store() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setReference("REF-STORE-002");
        paymentMethod.setBarcodeUrl("https://example.com/barcode2.png");

        Charge expectedCharge = new Charge();
        expectedCharge.setPaymentMethod(paymentMethod);

        when(chargeOperations.create(any(CreateStoreChargeParams.class))).thenReturn(expectedCharge);

        Charge transaction = this.api.charges().create(
                new CreateStoreChargeParams().amount(amount).description(desc)
                .orderId(orderId).customer(new Customer().name("Vivaldi").email("v@comerce.com").phoneNumber("154234623")));

        assertNotNull(transaction);
        assertNotNull(transaction.getPaymentMethod().getReference());
        assertNotNull(transaction.getPaymentMethod().getBarcodeUrl());
        Assert.assertNull(transaction.getFee());
    }
}
