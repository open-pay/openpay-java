/*
 * COPYRIGHT © 2012-2015. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.core.client.full;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.Test;

import mx.openpay.client.Bin;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class BinesTest extends BaseTest {

    @Test
    public void testGetMerchant() throws Exception {
        Bin bin = this.api.bines().get("415231");
        assertThat(bin.getBank()).isNotNull();
        assertThat(bin.getBin()).isNotNull();
        assertThat(bin.getBrand()).isNotNull();
        assertThat(bin.getCategory()).isNotNull();
        assertThat(bin.getCountryCode()).isNotNull();
        assertThat(bin.getType()).isNotNull();
        assertThat(bin.getAllowedOnline()).isNotNull();
        assertThat(bin.getAllowedSantanderPoints()).isNotNull();
    }
}
