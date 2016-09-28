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
package mx.openpay.client;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

/**
 * The Class PaymentPlan.
 *
 * @author ismael
 */

@Getter
@Setter
@ToString
public class PaymentPlan {

    /** The id. */
    private String id;

    /** The name. */
    @SerializedName("name")
    private String name;

    /** The first payment percentage. */
    @SerializedName("first_payment_percentage")
    private BigDecimal firstPaymentPercentage;

    /** The max number of payments. */
    @SerializedName("maximun_number_of_payments")
    private Integer maxNumberOfPayments;

    /** The months to pay. */
    @SerializedName("months_to_pay")
    private Integer monthsToPay;

    /** The status. */
    private String status;

    /** The days to first payment. */
    @SerializedName("days_to_first_payment")
    private Integer daysToFirstPayment;

    /**
     * Name.
     * @param name the name
     * @return the payment plan
     */
    public PaymentPlan name(final String name) {
        this.name = name;
        return this;
    }

    /**
     * First payment percentage.
     * @param firstPaymentPercentage the first payment percentage
     * @return the payment plan
     */
    public PaymentPlan firstPaymentPercentage(final BigDecimal firstPaymentPercentage) {
        this.firstPaymentPercentage = firstPaymentPercentage;
        return this;
    }

    /**
     * Max number of payments.
     * @param maxNumberOfPayments the max number of payments
     * @return the payment plan
     */
    public PaymentPlan maxNumberOfPayments(final Integer maxNumberOfPayments) {
        this.maxNumberOfPayments = maxNumberOfPayments;
        return this;
    }

    /**
     * Months to pay.
     * @param monthsToPay the months to pay
     * @return the payment plan
     */
    public PaymentPlan monthsToPay(final Integer monthsToPay) {
        this.monthsToPay = monthsToPay;
        return this;
    }

    /**
     * Days to first payment.
     * @param daysToFirstPayment the days to first payment
     * @return the payment plan
     */
    public PaymentPlan daysToFirstPayment(final Integer daysToFirstPayment) {
        this.daysToFirstPayment = daysToFirstPayment;
        return this;
    }
}
