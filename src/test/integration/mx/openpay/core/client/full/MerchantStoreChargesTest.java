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
import mx.openpay.client.core.requests.transactions.CreateStoreChargeParams;

import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class MerchantStoreChargesTest extends BaseTest {

    @Test
    public void testCreate_Merchant_Store() throws Exception {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().create(new CreateStoreChargeParams().amount(amount).description(desc)
                .orderId(orderId));
        assertNotNull(transaction);
        assertNotNull(transaction.getPaymentMethod().getReference());
        assertNotNull(transaction.getPaymentMethod().getBarcodeUrl());
    }

}
