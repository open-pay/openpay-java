/*
 * COPYRIGHT © 2012-2015. OPENPAY.
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
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Getter
@Setter
@ToString
public class Bin {

    private String bank;

    private String bin;

    private String brand;

    private String category;

    private String country;

    @SerializedName("country_code")
    private String countryCode;

    private String type;

    @SerializedName("allowed_online")
    private Boolean allowedOnline;

    @SerializedName("allowed_santander_points")
    private Boolean allowedSantanderPoints;

}
