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
