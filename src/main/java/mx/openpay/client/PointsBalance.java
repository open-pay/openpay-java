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
import java.math.BigInteger;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * @author oswaldo
 *
 */
@Getter
@Setter
public class PointsBalance {
    
    @SerializedName("remaining_points")
    public BigInteger remainingPoints;
    
    @SerializedName("remaining_mxn")
    public BigDecimal remainingMxn;

}
