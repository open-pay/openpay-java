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
package mx.openpay.client.core.operations.groups;

import static mx.openpay.client.utils.OpenpayPathComponents.CAPTURE;
import static mx.openpay.client.utils.OpenpayPathComponents.CHARGES;
import static mx.openpay.client.utils.OpenpayPathComponents.CONFIRM;
import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.GROUPS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANTS;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.REFUND;

import mx.openpay.client.Charge;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.operations.ServiceOperations;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * @author elopez
 */
public class GroupChargeOperations extends ServiceOperations {

    private static final String FOR_MERCHANT_PATH = GROUPS + ID + MERCHANTS + MERCHANT_ID + CHARGES;

    protected static final String GET_FOR_MERCHANT_PATH = FOR_MERCHANT_PATH + ID;

    private static final String REFUND_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + REFUND;

    private static final String CAPTURE_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + CAPTURE;

    private static final String CONFIRM_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + CONFIRM;

    private static final String FOR_CUSTOMER_PATH = GROUPS + ID + MERCHANTS + MERCHANT_ID + CUSTOMERS + ID + CHARGES;

    protected static final String GET_FOR_CUSTOMER_PATH = FOR_CUSTOMER_PATH + ID;

    private static final String REFUND_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + REFUND;

    private static final String CAPTURE_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + CAPTURE;

    private static final String CONFIRM_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + CONFIRM;

    public GroupChargeOperations(final JsonServiceClient client) {
        super(client);
    }

    public Charge create(final String merchantId, final CreateCardChargeParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId(), merchantId);
        return this.getJsonClient().post(path, request.asMap(), Charge.class);
    }

    public Charge create(final String merchantId, final String customerId, final CreateCardChargeParams request)
            throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), merchantId, customerId);
        return this.getJsonClient().post(path, request.asMap(), Charge.class);
    }

    public Charge refund(String merchantId, final RefundParams params)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_FOR_MERCHANT_PATH, this.getMerchantId(), merchantId, params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }

    public Charge refund(String merchantId, final String customerId, final RefundParams params)
            throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(REFUND_FOR_CUSTOMER_PATH, this.getMerchantId(), merchantId, customerId,
                params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }


}
