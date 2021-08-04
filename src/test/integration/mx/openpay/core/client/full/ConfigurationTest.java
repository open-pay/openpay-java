/*
 * Copyright 2013 Opencard Inc.
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

import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.TimeZone;

import org.junit.Test;

import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * @author elopez
 */
public class ConfigurationTest {

    @Test
    public void testNoAPIKey() throws Exception {
        OpenpayAPI api = new OpenpayAPI(ENDPOINT.replace("https", "http"), null, MERCHANT_ID);
        try {
            api.customers().list(null);
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(401, e.getHttpCode().intValue());
        }
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

    @Test
    public void testForceHttps() throws Exception {
        OpenpayAPI api = new OpenpayAPI(ENDPOINT.replace("https", "http"), API_KEY, MERCHANT_ID);
        assertNotNull(api.customers().list(null));
    }

    @Test(expected = ServiceUnavailableException.class)
    public void testNoConnection() throws Exception {
        OpenpayAPI api = new OpenpayAPI("http://localhost:9090", API_KEY, MERCHANT_ID);
        api.customers().list(null);
    }

    @Test
    public void testAddHttps() throws Exception {
        OpenpayAPI api = new OpenpayAPI(ENDPOINT.replace("https://", ""), API_KEY, MERCHANT_ID);
        assertNotNull(api.customers().list(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMerchant() throws Exception {
        new OpenpayAPI(ENDPOINT.replace("https://", ""), API_KEY, null);
    }

    @Test
    public void testWrongMerchant() throws Exception {
        OpenpayAPI api = new OpenpayAPI(ENDPOINT, API_KEY, "notexists");
        try {
            api.customers().list(null);
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(401, e.getHttpCode().intValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLocation() throws Exception {
        new OpenpayAPI(null, API_KEY, MERCHANT_ID);
    }

}
