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

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.core.requests.transactions.CreateStoreChargeParams;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class CustomerStoreChargesTest extends BaseTest {

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
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(this.customer.getId(),
                new CreateStoreChargeParams().amount(amount).description(desc).orderId(orderId));
        assertNotNull(transaction);
        assertNotNull(transaction.getPaymentMethod().getReference());
        assertNotNull(transaction.getPaymentMethod().getBarcodeUrl());
    }

}
