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

import java.math.BigDecimal;
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

}
