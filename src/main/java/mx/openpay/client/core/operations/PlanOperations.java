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

import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.PLANS;

import java.util.List;
import java.util.Map;

import mx.openpay.client.Plan;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.requests.subscription.CreatePlanParams;
import mx.openpay.client.core.requests.subscription.UpdatePlanParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class PlanOperations extends ServiceOperations {

    private static final String PLANS_PATH = MERCHANT_ID + PLANS;

    private static final String GET_PLAN_PATH = PLANS_PATH + ID;

    public PlanOperations(final JsonServiceClient client, final String merchantId) {
        super(client, merchantId);
    }

    public Plan create(final CreatePlanParams createPlan) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(PLANS_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, createPlan.asMap(), Plan.class);
    };

    public Plan update(final UpdatePlanParams updatePlan) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_PLAN_PATH, this.getMerchantId(), updatePlan.getPlanId());
        return this.getJsonClient().put(path, updatePlan.asMap(), Plan.class);
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

}
