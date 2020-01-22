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

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author elopez
 */
@Getter
@Setter
@ToString
public class Subscription {

    private String id;

    @SerializedName("creation_date")
    private Date creationDate;

    @SerializedName("cancel_at_period_end")
    private Boolean cancelAtPeriodEnd;

    @SerializedName("charge_date")
    private Date chargeDate;

    @SerializedName("current_period_number")
    private Integer currentPeriodNumber;

    @SerializedName("period_end_date")
    private Date periodEndDate;

    @SerializedName("trial_end_date")
    private Date trialEndDate;

    @SerializedName("plan_id")
    private String planId;

    private String status;

    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("source_id")
    private String sourceId;

    private Card card;
    
    private Transaction transaction; 

    /**
     * The ID of the plan to use for the subscription. Required.
     */
    public Subscription planId(final String planId) {
        this.planId = planId;
        return this;
    }

    /*
     *Use {@link #getSourceId()} instead.
     */
    public String getCardId() {
        return this.sourceId;
    }

    /*
     * Use {@link #setSourceId(String)} instead.
     * @param cardId
     */
    @Deprecated
    public void setCardId(final String cardId) {
        this.sourceId = cardId;
    }

    /**
     * The ID of the customer's pre-registered card to which the charge will be made each cycle. Required if no card is
     * specified to be created.
     * @deprecated use {@link #sourceId(String)} instead.
     */
    @Deprecated
    public Subscription cardId(final String cardId) {
        this.sourceId = cardId;
        return this;
    }

    /**
     * The ID of the customer's pre-registered card or token to which the charge will be made each cycle. Required if no
     * card is specified to be created.
     */
    public Subscription sourceId(final String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    /**
     * The day to end the trial, overriding the default trial_days. Optional. A past date means the subscription won't
     * have any trial days. The time part is ignored.
     */
    public Subscription trialEndDate(final Date trialEndDate) {
        this.trialEndDate = trialEndDate;
        return this;
    }

    /**
     * The card to which the charge will be made each cycle. Required if no pre-registered card id is specified.
     */
    public Subscription card(final Card card) {
        this.card = card;
        return this;
    }

}
