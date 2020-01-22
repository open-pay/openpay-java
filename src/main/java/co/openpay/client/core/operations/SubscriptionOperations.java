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
import static co.openpay.client.utils.OpenpayPathComponents.PLANS;
import static co.openpay.client.utils.OpenpayPathComponents.SUBSCRIPTIONS;

import java.util.List;
import java.util.Map;

import co.openpay.client.Subscription;
import co.openpay.client.core.JsonServiceClient;
import co.openpay.client.exceptions.OpenpayServiceException;
import co.openpay.client.exceptions.ServiceUnavailableException;
import co.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class SubscriptionOperations extends ServiceOperations {

    private static final String CUSTOMER_SUBSCRIPTIONS_PATH = MERCHANT_ID + CUSTOMERS + ID + SUBSCRIPTIONS;

    private static final String GET_CUSTOMER_SUBSCRIPTION_PATH = MERCHANT_ID + CUSTOMERS + ID + SUBSCRIPTIONS + ID;
	private static final String GET_PLAN_SUBSCRIPTION_PATH = MERCHANT_ID + PLANS + ID + SUBSCRIPTIONS + ID;

    public SubscriptionOperations(final JsonServiceClient client) {
        super(client);
    }

    public Subscription create(final String customerId, final Subscription request) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMER_SUBSCRIPTIONS_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().post(path, request, Subscription.class);
    };

    public Subscription update(final Subscription subscription) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_SUBSCRIPTION_PATH, this.getMerchantId(), subscription.getCustomerId(),
                subscription.getId());
        return this.getJsonClient().put(path, subscription, Subscription.class);
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

	public Subscription getByPlan(final String planId, final String subscriptionId) throws OpenpayServiceException,
			ServiceUnavailableException {
		String path = String.format(GET_PLAN_SUBSCRIPTION_PATH, this.getMerchantId(), planId, subscriptionId);
		return this.getJsonClient().get(path, Subscription.class);
	};

}
