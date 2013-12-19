/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
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
