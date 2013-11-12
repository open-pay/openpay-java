/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.core.client;

import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import mx.openpay.client.Customer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.Test;

/**
 * @author elopez
 */
public class ConfigurationTest {

    @Test
    public void testNoAPIKey() throws Exception {
        OpenpayAPI.configure(ENDPOINT, null, MERCHANT_ID);
        try {
            Customer.list(null);
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(401, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testForceHttps() throws Exception {
        OpenpayAPI.configure(ENDPOINT.replace("https", "http"), API_KEY, MERCHANT_ID);
        assertNotNull(Customer.list(null));
    }

    @Test(expected = ServiceUnavailableException.class)
    public void testNoConnection() throws Exception {
        OpenpayAPI.configure("http://localhost:9090", API_KEY, MERCHANT_ID);
        Customer.list(null);
    }

    @Test
    public void testAddHttps() throws Exception {
        OpenpayAPI.configure(ENDPOINT.replace("https://", ""), API_KEY, MERCHANT_ID);
        assertNotNull(Customer.list(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMerchant() throws Exception {
        OpenpayAPI.configure(ENDPOINT.replace("https://", ""), API_KEY, null);
    }

    @Test
    public void testWrongMerchant() throws Exception {
        OpenpayAPI.configure(ENDPOINT, API_KEY, "notexists");
        try {
            Customer.list(null);
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(401, e.getHttpCode().intValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLocation() throws Exception {
        OpenpayAPI.configure(null, API_KEY, MERCHANT_ID);
    }

}
