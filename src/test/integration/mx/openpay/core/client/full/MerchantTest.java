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
import mx.openpay.client.Merchant;

import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class MerchantTest extends BaseTest {

    @Test
    public void testGetMerchant() throws Exception {
        Merchant merchant = this.api.merchant().get();
        assertNotNull(merchant.getCreationDate());
        assertNotNull(merchant.getBalance());
        assertNotNull(merchant.getEmail());
        assertNotNull(merchant.getName());
        assertNotNull(merchant.getStatus());
        assertNotNull(merchant.getPhone());
        assertNotNull(merchant.getClabe());
        assertNotNull(merchant.getId());
    }
}
