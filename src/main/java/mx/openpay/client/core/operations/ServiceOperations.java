package mx.openpay.client.core.operations;

import mx.openpay.client.core.JsonServiceClient;

/**
 * @author elopez
 */
public abstract class ServiceOperations {

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
