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

import static co.openpay.client.utils.OpenpayPathComponents.ID;
import static co.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static co.openpay.client.utils.OpenpayPathComponents.PLANS;
import static co.openpay.client.utils.OpenpayPathComponents.SUBSCRIPTIONS;

import java.util.List;
import java.util.Map;

import co.openpay.client.Plan;
import co.openpay.client.Subscription;
import co.openpay.client.core.JsonServiceClient;
import co.openpay.client.exceptions.OpenpayServiceException;
import co.openpay.client.exceptions.ServiceUnavailableException;
import co.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class PlanOperations extends ServiceOperations {

    private static final String PLANS_PATH = MERCHANT_ID + PLANS;

    private static final String GET_PLAN_PATH = PLANS_PATH + ID;

	private static final String GET_SUBSCRIPTIONS_PLAN_PATH = GET_PLAN_PATH + SUBSCRIPTIONS;

    public PlanOperations(final JsonServiceClient client) {
        super(client);
    }

    public Plan create(final Plan plan) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(PLANS_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, plan, Plan.class);
    };

    public Plan update(final Plan plan) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_PLAN_PATH, this.getMerchantId(), plan.getId());
        return this.getJsonClient().put(path, plan, Plan.class);
    };

    public void delete(final String planId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_PLAN_PATH, this.getMerchantId(), planId);
        this.getJsonClient().delete(path);
    };

    public Plan get(final String planId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_PLAN_PATH, this.getMerchantId(), planId);
        return this.getJsonClient().get(path, Plan.class);
    };

    public List<Plan> list(final SearchParams params) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(PLANS_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Plan.class);
    };

	public List<Subscription> listSubscriptions(final String planId, final SearchParams params)
			throws OpenpayServiceException, ServiceUnavailableException {
		String path = String.format(GET_SUBSCRIPTIONS_PLAN_PATH, this.getMerchantId(), planId);
		Map<String, String> map = params == null ? null : params.asMap();
		return this.getJsonClient().list(path, map, Subscription.class);
	};

}
