/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client;

import java.math.BigDecimal;

import lombok.Getter;

import com.google.gson.annotations.SerializedName;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Getter
public class Merchant {

    private String id;

    private String name;

    private String email;

    private String phone;

    private String status;

    @SerializedName("creation_date")
    private String creationDate;

    private BigDecimal balance;

    private String clabe;

}
