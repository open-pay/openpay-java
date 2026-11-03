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

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import mx.openpay.client.Charge;
import mx.openpay.client.PaymentMethod;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.ChargeOperations;
import mx.openpay.client.core.requests.transactions.CreateStoreChargeParams;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@RunWith(MockitoJUnitRunner.class)
public class MerchantStoreChargesTest {

    @Mock
    private OpenpayAPI api;

    @Mock
    private ChargeOperations chargeOperations;

    @Before
    public void setUp() {
        when(api.charges()).thenReturn(chargeOperations);
    }

    @Test
    public void testCreate_Merchant_Store() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setReference("REF-MERCHANT-001");
        paymentMethod.setBarcodeUrl("https://example.com/merchant-barcode.png");

        Charge expectedCharge = new Charge();
        expectedCharge.setPaymentMethod(paymentMethod);

        when(chargeOperations.create(any(CreateStoreChargeParams.class))).thenReturn(expectedCharge);

        Charge transaction = this.api.charges().create(new CreateStoreChargeParams().amount(amount).description(desc)
                .orderId(orderId));

        assertNotNull(transaction);
        assertNotNull(transaction.getPaymentMethod().getReference());
        assertNotNull(transaction.getPaymentMethod().getBarcodeUrl());
        Assert.assertNull(transaction.getFee());
    }
}
