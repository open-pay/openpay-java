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
