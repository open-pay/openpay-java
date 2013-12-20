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
package mx.openpay.client;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

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

    @SerializedName("card_id")
    private String cardId;

    private Card card;

}
