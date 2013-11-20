/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.operations;

import mx.openpay.client.core.JsonServiceClient;

/**
 * @author elopez
 */
public class ServiceOperations {

    private final String merchantId;

    private final JsonServiceClient client;

    public ServiceOperations(final JsonServiceClient client, final String merchantId) {
        if (client == null || merchantId == null) {
            throw new IllegalArgumentException("JSON client and Merchant Id are required");
        }
        this.client = client;
        this.merchantId = merchantId;
    }

    protected JsonServiceClient getJsonClient() {
        return this.client;
    }

    protected String getMerchantId() {
        return this.merchantId;
    }

}
