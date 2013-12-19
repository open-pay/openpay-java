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

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * @author elopez
 */
public class UpdatePlan extends RequestBuilder {

    @Getter
    private final String planId;

    public UpdatePlan(final String planId) {
        this.planId = planId;
    }

    public UpdatePlan withName(final String name) {
        return this.with("name", name);
    }

    public UpdatePlan withTrialDays(final Integer trialDays) {
        return this.with("trial_days", trialDays);
    }
}
