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

import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.GROUPS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANTS;
import static mx.openpay.client.utils.OpenpayPathComponents.SUBSCRIPTIONS;

import mx.openpay.client.Subscription;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.operations.ServiceOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/***
 * @author OpenpayMX
 */
public class GroupSubscriptionOperations extends ServiceOperations {

    private static final String GROUP_SUBSCRIPTIONS_PATH = GROUPS + ID + MERCHANTS + ID + CUSTOMERS + ID + SUBSCRIPTIONS;
    
    private static final String GROUP_SUBSCRIPTIONS_PATH_WITH_ID = GROUP_SUBSCRIPTIONS_PATH + ID;

    public GroupSubscriptionOperations(final JsonServiceClient client) {
        super(client);
    }


    public Subscription create(final String merchantId, final String customerId, final Subscription subscriptionRequest)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GROUP_SUBSCRIPTIONS_PATH, this.getMerchantId(), merchantId, customerId);
        return this.getJsonClient().post(path, subscriptionRequest, Subscription.class);
    }

    public void delete(final String merchantId, final String customerId, final String subscriptionId)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GROUP_SUBSCRIPTIONS_PATH_WITH_ID, this.getMerchantId(), merchantId, customerId,
                subscriptionId);
        this.getJsonClient().delete(path);
    }

}
