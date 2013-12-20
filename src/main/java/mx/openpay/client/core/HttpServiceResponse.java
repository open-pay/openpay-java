/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core;

import lombok.Getter;
import lombok.Setter;

/**
 * @author elopez
 */
@Getter
@Setter
public class HttpServiceResponse {

    private static final String JSON_MIME_TYPE = "application/json";

    private String contentType;

    private int statusCode;

    private String body;

    public boolean isJson() {
        return this.contentType != null && this.contentType.startsWith(JSON_MIME_TYPE);
    }
}
