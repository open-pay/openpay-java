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

import static mx.openpay.client.utils.OpenpayPathComponents.VERSION;

import java.security.GeneralSecurityException;

/**
 * @author elopez
 */
public class OpenpayAPI {

    private static final String HTTP_RESOURCE_SEPARATOR = "/";

    private static String _location;

    private static String _merchantId;

    private static String _apiKey;

    private static JsonServiceClient _jsonClient;

    public static void configure(final String location, final String apiKey, final String merchantId) {
        if (location == null) {
            throw new IllegalArgumentException("Location can't be null");
        }
        if (merchantId == null) {
            throw new IllegalArgumentException("Merchant ID can't be null");
        }
        OpenpayAPI._location = location;
        OpenpayAPI._merchantId = merchantId;
        OpenpayAPI._apiKey = apiKey;
        StringBuilder baseUri = new StringBuilder();
        if (location.contains("http") || location.contains("https")) {
            baseUri.append(location.replace("http:", "https:"));
        } else {
            baseUri.append("https://").append(location);
        }
        if (!location.endsWith(HTTP_RESOURCE_SEPARATOR)) {
            baseUri.append(HTTP_RESOURCE_SEPARATOR);
        }
        baseUri.append(VERSION);
        try {
            OpenpayAPI._jsonClient = new JsonServiceClient(baseUri.toString(), apiKey);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Can't initialize Openpay Client", e);
        }
    }

    public static String getLocation() {
        return _location;
    }

    public static String getMerchantId() {
        return _merchantId;
    }

    public static String getAPIKey() {
        return _apiKey;
    }

    public static JsonServiceClient getJsonClient() {
        if (_jsonClient == null) {
            throw new IllegalStateException("The client hasn't been configured yet.");
        }
        return _jsonClient;
    }

    public static void setTimeout(final int timeout) {
        if (_jsonClient == null) {
            throw new IllegalStateException("The client hasn't been configured yet.");
        }
        _jsonClient.setConnectionTimeout(timeout);
    }
}
