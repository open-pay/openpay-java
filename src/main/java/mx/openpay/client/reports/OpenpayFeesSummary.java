/*
 * Copyright 2014 Opencard Inc.
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
package mx.openpay.client.reports;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

/**
 * Summary of fees charged by Openpay during a given period.
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Getter
@Setter
@ToString
public class OpenpayFeesSummary {

    /**
     * Fees charged by Openpay on the transactions created by the merchant.
     */
    private BigDecimal charged;

    /**
     * Tax charged on the fees. (Value-added tax)
     */
    @SerializedName("charged_tax")
    private BigDecimal chargedTax;

    /**
     * Fees charged by Openpay for adjustments or other reasons.
     */
    @SerializedName("charged_adjustments")
    private BigDecimal chargedAdjustments;

    /**
     * Tax charged by Openpay on fees for adjustments or other reasons.
     */
    @SerializedName("charged_adjustments_tax")
    private BigDecimal chargedAdjustmentsTax;

    /**
     * Fees refunded to the merchant for refunded transactions.
     */
    private BigDecimal refunded;

    /**
     * Tax refunded to the merchant for refunded transactions.
     */
    @SerializedName("refunded_tax")
    private BigDecimal refundedTax;

    /**
     * Fees refunded to the merchant for adjustments or other reasons.
     */
    @SerializedName("refunded_adjustments")
    private BigDecimal refundedAdjustments;

    /**
     * Tax refunded to the merchant for adjustments or other reasons.
     */
    @SerializedName("refunded_adjustments_tax")
    private BigDecimal refundedAdjustmentsTax;

    /**
     * Total fees charged by Openpay. If the refunded fees are more than the charged fees, the total will be negative.
     */
    private BigDecimal total;

}
