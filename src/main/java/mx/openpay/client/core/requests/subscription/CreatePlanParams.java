/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.requests.plan;

import java.math.BigDecimal;

import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.PlanRepeatUnit;
import mx.openpay.client.enums.PlanStatusAfterRetry;

/**
 * Request parameters to create a plan.
 * @author elopez
 */
public class CreatePlanParams extends RequestBuilder {

    public CreatePlanParams name(final String name) {
        return this.with("name", name);
    }

    public CreatePlanParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * Convenience method to set both repeat_every and repeat_unit in one call.
     * @param repeatEvery
     * @param unit
     * @return
     */
    public CreatePlanParams repeatEvery(final Integer repeatEvery, final PlanRepeatUnit unit) {
        return this.repeatEvery(repeatEvery).repeatUnit(unit);
    }

    public CreatePlanParams trialDays(final Integer trialDays) {
        return this.with("trial_days", trialDays);
    }

    public CreatePlanParams retryTimes(final Integer retryTimes) {
        return this.with("retry_times", retryTimes);
    }

    public CreatePlanParams statusAfterRetry(final PlanStatusAfterRetry statusAfterRetry) {
        if (statusAfterRetry == null) {
            return this.with("status_after_retry", null);
        } else {
            return this.with("status_after_retry", statusAfterRetry.name().toLowerCase());
        }
    }

    private CreatePlanParams repeatEvery(final Integer repeatEvery) {
        return this.with("repeat_every", repeatEvery);
    }

    private CreatePlanParams repeatUnit(final PlanRepeatUnit unit) {
        if (unit == null) {
            return this.with("repeat_unit", null);
        } else {
            return this.with("repeat_unit", unit.name().toLowerCase());
        }
    }

}
