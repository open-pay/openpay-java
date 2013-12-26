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
import static mx.openpay.client.utils.OpenpayPathComponents.SUBSCRIPTIONS;

import java.util.List;
import java.util.Map;

import mx.openpay.client.Subscription;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.requests.subscription.CreateSubscriptionParams;
import mx.openpay.client.core.requests.subscription.UpdateSubscriptionParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class SubscriptionOperations extends ServiceOperations {

    private static final String CUSTOMER_SUBSCRIPTIONS_PATH = MERCHANT_ID + CUSTOMERS + ID + SUBSCRIPTIONS;

    private static final String GET_CUSTOMER_SUBSCRIPTION_PATH = MERCHANT_ID + CUSTOMERS + ID + SUBSCRIPTIONS + ID;

    public SubscriptionOperations(final JsonServiceClient client, final String merchantId) {
        super(client, merchantId);
    }

    public Subscription create(final CreateSubscriptionParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMER_SUBSCRIPTIONS_PATH, this.getMerchantId(), request.getCustomerId());
        return this.getJsonClient().post(path, request.asMap(), Subscription.class);
    };

    public Subscription update(final UpdateSubscriptionParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_SUBSCRIPTION_PATH, this.getMerchantId(), request.getCustomerId(),
                request.getSubscriptionId());
        return this.getJsonClient().put(path, request.asMap(), Subscription.class);
    };

    public void delete(final String customerId, final String subscriptionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_SUBSCRIPTION_PATH, this.getMerchantId(), customerId, subscriptionId);
        this.getJsonClient().delete(path);
    };

    public Subscription get(final String customerId, final String subscriptionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_SUBSCRIPTION_PATH, this.getMerchantId(), customerId, subscriptionId);
        return this.getJsonClient().get(path, Subscription.class);
    };

    public List<Subscription> list(final String customerId, final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMER_SUBSCRIPTIONS_PATH, this.getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Subscription.class);
    };

}
