/*
 * Copyright 2021 Openpay SA de CV.
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

import mx.openpay.client.Charge;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import static mx.openpay.client.utils.OpenpayPathComponents.CHARGES;
import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;

/**
 *@author sergio.quinonez
 */
public class LendingOperations extends ServiceOperations {

    /**
     * URI for merchant charge endpoint
     */
    private static final String FOR_MERCHANT_PATH = MERCHANT_ID + CHARGES;
    /**
     *URI for customer charge endpoint
     */
    private static final String FOR_CUSTOMER_PATH = MERCHANT_ID + CUSTOMERS + ID + CHARGES;

    /**
     * @param client
     */
    public LendingOperations(final JsonServiceClient client) {
        super(client);
    }

    /**
     * Creates checkout at the Merchant level.
     *
     * @param request
     * @return
     * @throws OpenpayServiceException
     * @throws ServiceUnavailableException
     */
    public Charge createCharge(RequestBuilder request) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, request.asMap(), Charge.class);
    }

    /**
     * Creates checkout at the Customer level.
     *
     * @param customerId
     * @param request
     * @return
     * @throws OpenpayServiceException
     * @throws ServiceUnavailableException
     */
    public Charge createCharge(final String customerId, RequestBuilder request)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().post(path, request.asMap(), Charge.class);
    }
}
