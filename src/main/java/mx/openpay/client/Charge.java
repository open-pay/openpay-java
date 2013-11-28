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

import lombok.Getter;
import lombok.Setter;

import com.google.gson.annotations.SerializedName;

/**
 * @author elopez
 */
@Getter
@Setter
public class Charge extends Transaction {

    private Refund refund;

    @SerializedName("payment_method")
    private PaymentMethod paymentMethod;

}
