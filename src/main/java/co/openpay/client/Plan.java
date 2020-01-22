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
package co.openpay.client;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

import co.openpay.client.enums.PlanRepeatUnit;
import co.openpay.client.enums.PlanStatusAfterRetry;

/**
 * @author elopez
 */
@Getter
@Setter
@ToString
public class Plan {

    private String id;

    @SerializedName("creation_date")
    private Date creationDate;

    private String name;

    private BigDecimal amount;

    private String currency;

    @SerializedName("repeat_every")
    private Integer repeatEvery;

    @SerializedName("repeat_unit")
    private String repeatUnit;

    @SerializedName("retry_times")
    private Integer retryTimes;

    private String status;

    @SerializedName("status_after_retry")
    private String statusAfterRetry;

    @SerializedName("trial_days")
    private Integer trialDays;

    /**
     * A name to identify the bank account. Required.
     */
    public Plan name(final String name) {
        this.name = name;
        return this;
    }

    /**
     * The amount to charge in each cycle, in MXN. Required.
     */
    public Plan amount(final BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    /**
     * The duration of each cycle. Required.
     */
    public Plan repeatEvery(final Integer repeatEvery, final PlanRepeatUnit unit) {
        this.repeatEvery = repeatEvery;
        this.repeatUnit = unit == null ? null : unit.name().toLowerCase();
        return this;
    }

    /**
     * Number of trial days for this plan. Optional.
     */
    public Plan trialDays(final Integer trialDays) {
        this.trialDays = trialDays;
        return this;
    }

    /**
     * Number of charge attempts before the subscription's state changes to either unpaid or cancelled. Optional.
     */
    public Plan retryTimes(final Integer retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    /**
     * The status to put the subscription in if there are not enough. Required.
     * <p>
     * UNPAID means that the charge will be attempted once again if the card information changes, while CANCELLED means
     * that no further charges will be attempted and the subscription will be cancelled.
     * </p>
     */
    public Plan statusAfterRetry(final PlanStatusAfterRetry statusAfterRetry) {
        this.statusAfterRetry = statusAfterRetry == null ? null : statusAfterRetry.name().toLowerCase();
        return this;
    }
}
