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
import lombok.ToString;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Getter
@ToString
public class CardPoints {

    private BigDecimal used;

    private BigDecimal remaining;

    private BigDecimal amount;

    private String caption;

}
