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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import co.openpay.client.Charge;
import co.openpay.client.Customer;
import co.openpay.client.core.requests.transactions.CreateBitcoinChargeParams;

/**
 * @author Oswaldo Martinez oswaldo.martinez@openpay.mx
 */
@Ignore("Bitcoin charges unavailable")
public class CustomerBitcoinChargesTest extends BaseTest {

    private Customer customer;

    @Before
    public void setUp() throws Exception {
        this.customer = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
    }

    @After
    public void tearDown() throws Exception {
        this.api.customers().delete(this.customer.getId());
    }

    @Test
    public void testCreate_Customer_Store() throws Exception {
        BigDecimal amount = new BigDecimal("100.00");
        String desc = "Pago de servicio";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(this.customer.getId(),
                new CreateBitcoinChargeParams().amount(amount).description(desc).orderId(orderId));
        assertNotNull(transaction);
        assertEquals(transaction.getPaymentMethod().getType(), "bitcoin");
        assertEquals(transaction.getAmount().intValue(), amount.intValue());
        assertNotNull(transaction.getPaymentMethod().getPaymentAddress());
        assertNotNull(transaction.getPaymentMethod().getPaymentUrlBip21());
        assertNotNull(transaction.getPaymentMethod().getAmountBitcoins());
        assertNotNull(transaction.getPaymentMethod().getExchangeRate());
        assertNotNull(transaction.getStatus(), "CHARGE_PENDING");
        Assert.assertNull(transaction.getFee());
    }

    @Test
    public void testCreate_SingleCustomer_Store() throws Exception {
        BigDecimal amount = new BigDecimal("100.00");
        String desc = "Pago de servicio";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(
                new CreateBitcoinChargeParams().amount(amount).description(desc)
                .orderId(orderId).customer(new Customer().name("Vivaldi").email("v@comerce.com").phoneNumber("154234623")));
        assertNotNull(transaction);
        assertEquals(transaction.getPaymentMethod().getType(), "bitcoin");
        assertEquals(transaction.getAmount().intValue(), amount.intValue());
        assertNotNull(transaction.getPaymentMethod().getPaymentAddress());
        assertNotNull(transaction.getPaymentMethod().getPaymentUrlBip21());
        assertNotNull(transaction.getPaymentMethod().getAmountBitcoins());
        assertNotNull(transaction.getPaymentMethod().getExchangeRate());
        assertNotNull(transaction.getStatus(), "CHARGE_PENDING");
        Assert.assertNull(transaction.getFee());
    }
    
}
