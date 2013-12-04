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
