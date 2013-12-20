/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.requests.bank;

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * @author elopez
 */
public class CreateBankAccountParams extends RequestBuilder {

    @Getter
    private String customerId;

    public CreateBankAccountParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreateBankAccountParams holderName(final String holderName) {
        return this.with("holder_name", holderName);
    }

    public CreateBankAccountParams clabe(final String clabe) {
        return this.with("clabe", clabe);
    }

    public CreateBankAccountParams alias(final String alias) {
        return this.with("alias", alias);
    }

}
