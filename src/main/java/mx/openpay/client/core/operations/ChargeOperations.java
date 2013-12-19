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

import static mx.openpay.client.utils.OpenpayPathComponents.CHARGES;
import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.REFUND;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.requests.charge.CreateBankTransferCharge;
import mx.openpay.client.core.requests.charge.CreateCardCharge;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.ListTypes;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class ChargeOperations extends ServiceOperations {

    private static final String FOR_MERCHANT_PATH = MERCHANT_ID + CHARGES;

    private static final String GET_FOR_MERCHANT_PATH = FOR_MERCHANT_PATH + ID;

    private static final String REFUND_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + REFUND;

    private static final String FOR_CUSTOMER_PATH = MERCHANT_ID + CUSTOMERS + ID + CHARGES;

    private static final String GET_FOR_CUSTOMER_PATH = FOR_CUSTOMER_PATH + ID;

    private static final String REFUND_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + REFUND;

    public ChargeOperations(final JsonServiceClient client, final String merchantId) {
        super(client, merchantId);
    }

    public Charge create(final CreateCardCharge request) throws OpenpayServiceException, ServiceUnavailableException {
        String path;
        if (request.getCustomerId() == null) {
            path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        } else {
            path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), request.getCustomerId());
        }
        return this.getJsonClient().post(path, request.asMap(), Charge.class);
    }

    @Deprecated
    public Charge create(final Card card, final BigDecimal amount, final String description,
            final String orderId) throws OpenpayServiceException, ServiceUnavailableException {
        CreateCardCharge chargeRequest = new CreateCardCharge()
                .withCard(card).withAmount(amount)
                .withDescription(description).withOrderId(orderId);
        return this.create(chargeRequest);
    }

    @Deprecated
    public Charge create(final String customerId, final Card card, final BigDecimal amount,
            final String description, final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        CreateCardCharge charge = new CreateCardCharge()
                .withCustomerId(customerId)
                .withCard(card)
                .withAmount(amount)
                .withDescription(description)
                .withOrderId(orderId);
        return this.create(charge);
    }

    @Deprecated
    public Charge create(final String customerId, final String sourceId, final BigDecimal amount,
            final String description, final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        CreateCardCharge charge = new CreateCardCharge()
                .withCustomerId(customerId)
                .withCardId(sourceId)
                .withAmount(amount)
                .withDescription(description)
                .withOrderId(orderId);
        return this.create(charge);
    }

    public Charge create(final CreateBankTransferCharge request) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path;
        if (request.getCustomerId() == null) {
            path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        } else {
            path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), request.getCustomerId());
        }
        return this.getJsonClient().post(path, request.asMap(), Charge.class);
    }

    @Deprecated
    public Charge createMerchantBankTransfer(final BigDecimal amount, final String description, final String orderId)
            throws OpenpayServiceException, ServiceUnavailableException {
        CreateBankTransferCharge request = new CreateBankTransferCharge()
                .withAmount(amount)
                .withDescription(description)
                .withOrderId(orderId);
        return this.create(request);
    }

    @Deprecated
    public Charge createCustomerBankTransfer(final String customerId, final BigDecimal amount,
            final String description,
            final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        CreateBankTransferCharge request = new CreateBankTransferCharge()
                .withCustomerId(customerId)
                .withAmount(amount)
                .withDescription(description)
                .withOrderId(orderId);
        return this.create(request);
    }

    public List<Charge> list(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, ListTypes.CHARGE);
    }

    public Charge get(final String transactionId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_FOR_MERCHANT_PATH, this.getMerchantId(), transactionId);
        return this.getJsonClient().get(path, Charge.class);
    }

    public Charge refund(final String transactionId, final String description, final String orderId)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_FOR_MERCHANT_PATH, this.getMerchantId(), transactionId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("description", description);
        data.put("order_id", orderId);
        return this.getJsonClient().post(path, data, Charge.class);
    }

    public Charge refund(final String customerId, final String transactionId, final String description,
            final String orderId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, transactionId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("description", description);
        data.put("order_id", orderId);
        return this.getJsonClient().post(path, data, Charge.class);
    }

    public List<Charge> list(final String customerId, final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, ListTypes.CHARGE);
    }

    public Charge get(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, transactionId);
        return this.getJsonClient().get(path, Charge.class);
    }

}
