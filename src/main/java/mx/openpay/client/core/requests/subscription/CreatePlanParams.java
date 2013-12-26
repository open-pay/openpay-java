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
package mx.openpay.client.core.requests.subscription;

import java.math.BigDecimal;

import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.PlanRepeatUnit;
import mx.openpay.client.enums.PlanStatusAfterRetry;

/**
 * Request parameters to create a plan.
 * @author elopez
 */
public class CreatePlanParams extends RequestBuilder {

    /**
     * A name to identify the bank account. Required.
     */
    public CreatePlanParams name(final String name) {
        return this.with("name", name);
    }

    /**
     * The amount to charge in each cycle, in MXN. Required.
     */
    public CreatePlanParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * The duration of each cycle. Required.
     */
    public CreatePlanParams repeatEvery(final Integer repeatEvery, final PlanRepeatUnit unit) {
        return this.repeatEvery(repeatEvery).repeatUnit(unit);
    }

    /**
     * Number of trial days for this plan. Optional.
     */
    public CreatePlanParams trialDays(final Integer trialDays) {
        return this.with("trial_days", trialDays);
    }

    /**
     * Number of charge attempts before the subscription's state changes to either unpaid or cancelled. Optional.
     */
    public CreatePlanParams retryTimes(final Integer retryTimes) {
        return this.with("retry_times", retryTimes);
    }

    /**
     * The status to put the subscription in if there are not enough. Required.
     * <p>
     * UNPAID means that the charge will be attempted once again if the card information changes, while CANCELLED means
     * that no further charges will be attempted and the subscription will be cancelled.
     * </p>
     */
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
