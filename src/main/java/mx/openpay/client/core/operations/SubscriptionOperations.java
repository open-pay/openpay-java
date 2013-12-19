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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.Card;
import mx.openpay.client.Subscription;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.ListTypes;
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

    public Subscription create(final String customerId, final String planId, final String cardId,
            final Integer trialDays) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(CUSTOMER_SUBSCRIPTIONS_PATH, this.getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("plan_id", planId);
        data.put("card_id", cardId);
        data.put("trial_days", trialDays);
        return this.getJsonClient().post(path, data, Subscription.class);
    };

    public Subscription create(final String customerId, final String planId, final Card card,
            final Integer trialDays) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(CUSTOMER_SUBSCRIPTIONS_PATH, this.getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("plan_id", planId);
        data.put("card", card);
        data.put("trial_days", trialDays);
        return this.getJsonClient().post(path, data, Subscription.class);
    };

    public Subscription update(final String customerId, final String subscriptionId, final Date trialEndDate,
            final Boolean cancelAtPeriodEnd, final String cardId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_SUBSCRIPTION_PATH, this.getMerchantId(), customerId, subscriptionId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("trial_end_date", trialEndDate);
        data.put("cancel_at_period_end", cancelAtPeriodEnd);
        data.put("card_id", cardId);
        return this.getJsonClient().put(path, data, Subscription.class);
    };

    public Subscription update(final String customerId, final String subscriptionId, final Date trialEndDate,
            final Boolean cancelAtPeriodEnd, final Card card) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_SUBSCRIPTION_PATH, this.getMerchantId(), customerId, subscriptionId);
        Map<String, Object> data = new HashMap<String, Object>();
        if (trialEndDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            data.put("trial_end_date", format.format(trialEndDate));
        }
        data.put("cancel_at_period_end", cancelAtPeriodEnd);
        data.put("card", card);
        return this.getJsonClient().put(path, data, Subscription.class);
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
        return this.getJsonClient().list(path, map, ListTypes.SUBSCRIPTIONS);
    };

}
