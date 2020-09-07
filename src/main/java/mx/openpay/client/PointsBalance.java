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
import mx.openpay.client.enums.PointsType;

/**
 * @author oswaldo
 *
 */
@Getter
@Setter
public class PointsBalance {
    
    @SerializedName("points_type")
    private String pointsType;
    
    @SerializedName("remaining_points")
    private BigInteger remainingPoints;
    
    @SerializedName("remaining_mxn")
    private BigDecimal remainingMxn;

    public PointsType getPointsType() {
        return PointsType.valueOf(this.pointsType.toUpperCase());
    }

}
