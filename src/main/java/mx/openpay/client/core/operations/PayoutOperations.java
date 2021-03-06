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

import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.PAYOUTS;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Payout;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.requests.transactions.CreateBankPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateCardPayoutParams;
import mx.openpay.client.enums.PayoutMethod;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class PayoutOperations extends ServiceOperations {

    private static final String FOR_MERCHANT_PATH = MERCHANT_ID + PAYOUTS;

    private static final String GET_FOR_MERCHANT_PATH = FOR_MERCHANT_PATH + ID;

    private static final String FOR_CUSTOMER_PATH = MERCHANT_ID + CUSTOMERS + ID + PAYOUTS;

    private static final String GET_FOR_CUSTOMER_PATH = FOR_CUSTOMER_PATH + ID;

    /**
     * @param client
     */
    public PayoutOperations(final JsonServiceClient client) {
        super(client);
    }

    public Payout create(final CreateBankPayoutParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, request.asMap(), Payout.class);
    }

    public Payout create(final String customerId, final CreateBankPayoutParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().post(path, request.asMap(), Payout.class);
    }

    public Payout create(final CreateCardPayoutParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, request.asMap(), Payout.class);
    }

    public Payout create(final String customerId, final CreateCardPayoutParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().post(path, request.asMap(), Payout.class);
    }

    public Payout get(final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_MERCHANT_PATH, this.getMerchantId(), transactionId);
        return this.getJsonClient().get(path, Payout.class);
    }

    public Payout get(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, transactionId);
        return this.getJsonClient().get(path, Payout.class);
    }

    public void cancel(final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_MERCHANT_PATH, this.getMerchantId(), transactionId);
        this.getJsonClient().delete(path);
    }

    public void cancel(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, transactionId);
        this.getJsonClient().delete(path);
    }

    public List<Payout> list(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Payout.class);
    }

    public List<Payout> list(final String customerId, final SearchParams params)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Payout.class);
    }

    @Deprecated
    public Payout createForCustomer(final String customerId, final PayoutMethod method, final String destinationId,
            final BigDecimal amount, final String description, final String orderID)
            throws ServiceUnavailableException, OpenpayServiceException {
        switch (method) {
        case BANK_ACCOUNT:
            return this.create(customerId, new CreateBankPayoutParams()
                    .description(description)
                    .amount(amount)
                    .orderId(orderID)
                    .bankAccountId(destinationId));
        case CARD:
            return this.create(customerId, new CreateCardPayoutParams()
                    .description(description)
                    .amount(amount)
                    .orderId(orderID)
                    .cardId(destinationId));
        default:
            throw new IllegalArgumentException();
        }
    }

    @Deprecated
    public Payout createForCustomer(final String customerId, final Card card,
            final BigDecimal amount, final String description, final String orderID) throws OpenpayServiceException,
            ServiceUnavailableException {
        CreateCardPayoutParams request = new CreateCardPayoutParams()
                .description(description)
                .amount(amount)
                .orderId(orderID)
                .card(card);
        return this.create(customerId, request);
    }

    @Deprecated
    public Payout createForCustomer(final String customerId, final BankAccount bankAccount,
            final BigDecimal amount, final String description, final String orderID) throws OpenpayServiceException,
            ServiceUnavailableException {
        CreateBankPayoutParams request = new CreateBankPayoutParams()
                .description(description)
                .amount(amount)
                .orderId(orderID)
                .bankAccount(bankAccount);
        return this.create(customerId, request);
    }

    @Deprecated
    public Payout createForMerchant(final Card card, final BigDecimal amount,
            final String description, final String orderID) throws OpenpayServiceException, ServiceUnavailableException {
        CreateCardPayoutParams request = new CreateCardPayoutParams()
                .description(description)
                .amount(amount)
                .orderId(orderID)
                .card(card);
        return this.create(request);
    }

    @Deprecated
    public Payout createForMerchant(final BankAccount bankAccount, final BigDecimal amount,
            final String description, final String orderID) throws OpenpayServiceException, ServiceUnavailableException {
        CreateBankPayoutParams request = new CreateBankPayoutParams()
                .description(description)
                .amount(amount)
                .orderId(orderID)
                .bankAccount(bankAccount);
        return this.create(request);
    }

}
