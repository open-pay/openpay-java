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

import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.PLANS;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.Plan;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.enums.PlanRepeatUnit;
import mx.openpay.client.enums.PlanStatusAfterRetry;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.ListTypes;
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

    public Plan create(final String name, final BigDecimal amount, final String currency, final Integer repeatEvery,
            final PlanRepeatUnit repeatUnit, final Integer retryTimes, final PlanStatusAfterRetry statusAfterRetry,
            final Integer trialDays) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(PLANS_PATH, this.getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        data.put("amount", amount);
        data.put("currency", currency);
        data.put("repeat_every", repeatEvery);
        if (repeatUnit != null) {
            data.put("repeat_unit", repeatUnit.name().toLowerCase());
        }
        data.put("retry_times", retryTimes);
        if (statusAfterRetry != null) {
            data.put("status_after_retry", statusAfterRetry.name().toLowerCase());
        }
        data.put("trial_days", trialDays);
        return this.getJsonClient().post(path, data, Plan.class);
    };

    public Plan update(final String planId, final String name, final Integer trialDays) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_PLAN_PATH, this.getMerchantId(), planId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        data.put("trial_days", trialDays);
        return this.getJsonClient().put(path, data, Plan.class);
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
        return this.getJsonClient().list(path, map, ListTypes.PLANS);
    };

}
