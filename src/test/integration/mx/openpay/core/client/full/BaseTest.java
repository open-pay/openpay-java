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

import java.util.TimeZone;

import mx.openpay.client.core.OpenpayAPI;

import org.junit.Before;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class BaseTest {

    protected OpenpayAPI api;

    @Before
    public void setupAPI() throws Exception {
        String merchantId = "mtfsdeoulmcoj0xofpfc";
        String apiKey = "sk_4ec3ef18cd01471487ca719f566d4d3f";
        String endpoint = "https://localhost:8443/Services/";
        this.api = new OpenpayAPI(endpoint, apiKey, merchantId);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

}
