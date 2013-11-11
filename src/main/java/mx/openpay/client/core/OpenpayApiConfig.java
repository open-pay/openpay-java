/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core;

import static mx.openpay.client.utils.OpenpayPaths.VERSION;

/**
 * @author elopez
 */
public class OpenpayApiConfig {

    private static final String HTTP_RESOURCE_SEPARATOR = "/";

    private static String _location;

    private static String _merchantId;

    private static String _apiKey;

    private static JsonServiceClient _jsonClient;

    public static void configure(final String location, final String apiKey, final String merchantId) {
        OpenpayApiConfig._location = location;
        OpenpayApiConfig._merchantId = merchantId;
        OpenpayApiConfig._apiKey = apiKey;
        if (location.endsWith(HTTP_RESOURCE_SEPARATOR)) {
            OpenpayApiConfig._jsonClient = new JsonServiceClient(location + VERSION, apiKey);
        } else {
            OpenpayApiConfig._jsonClient = new JsonServiceClient(location + HTTP_RESOURCE_SEPARATOR + VERSION, apiKey);
        }

    }

    public static String getLocation() {
        return _location;
    }

    public static String getMerchantId() {
        return _merchantId;
    }

    public static String getApiKey() {
        return _apiKey;
    }

    public static JsonServiceClient getJsonClient() {
        if (_jsonClient == null) {
            throw new IllegalStateException("The client hasn't been configured yet.");
        }
        return _jsonClient;
    }
}
