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
package co.openpay.client.core.operations;

import static co.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static co.openpay.client.utils.OpenpayPathComponents.ID;
import static co.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static co.openpay.client.utils.OpenpayPathComponents.TRANSFERS;

import java.math.BigDecimal;
import java.util.List;

import co.openpay.client.Transfer;
import co.openpay.client.core.JsonServiceClient;
import co.openpay.client.core.requests.transactions.CreateTransferParams;
import co.openpay.client.exceptions.OpenpayServiceException;
import co.openpay.client.exceptions.ServiceUnavailableException;
import co.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class TransferOperations extends ServiceOperations {

    private static final String CUSTOMER_TRANSFERS_PATH = MERCHANT_ID + CUSTOMERS + ID + TRANSFERS;

    private static final String GET_CUSTOMER_TRANSFER_PATH = CUSTOMER_TRANSFERS_PATH + ID;

    /**
     * @param client
     */
    public TransferOperations(final JsonServiceClient client) {
        super(client);
    }

    public Transfer create(final String fromCustomerId, final CreateTransferParams params)
            throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMER_TRANSFERS_PATH, this.getMerchantId(), fromCustomerId);
        return this.getJsonClient().post(path, params.asMap(), Transfer.class);
    }

    @Deprecated
    public Transfer create(final String fromCustomerId, final String toCustomerId, final BigDecimal amount,
            final String description, final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        return this.create(fromCustomerId, new CreateTransferParams()
                .toCustomerId(toCustomerId)
                .amount(amount)
                .description(description)
                .orderId(orderId));
    }

    public List<Transfer> list(final String customerId, final SearchParams params)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(CUSTOMER_TRANSFERS_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().list(path, params == null ? null : params.asMap(), Transfer.class);
    }

    public Transfer get(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_TRANSFER_PATH, this.getMerchantId(), customerId, transactionId);
        return this.getJsonClient().get(path, Transfer.class);
    }

}
