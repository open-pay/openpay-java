/*
 * COPYRIGHT © 2012-2015. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package co.openpay.core.client.full;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import co.openpay.client.Bin;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class BinesTest extends BaseTest {

    @Test
    public void testGetMerchant() throws Exception {
        Bin bin = this.api.bines().get("415231");
        assertThat(bin.getBank(), is(notNullValue()));
        assertThat(bin.getBin(), is(notNullValue()));
        assertThat(bin.getBrand(), is(notNullValue()));
        assertThat(bin.getCategory(), is(notNullValue()));
        assertThat(bin.getCountryCode(), is(notNullValue()));
        assertThat(bin.getType(), is(notNullValue()));
        assertThat(bin.getAllowedOnline(), is(notNullValue()));
        assertThat(bin.getAllowedSantanderPoints(), is(notNullValue()));
    }
}
