/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.requests.card;

import lombok.Getter;
import mx.openpay.client.Address;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * @author elopez
 */
public class CreateCardParams extends RequestBuilder {

    @Getter
    private String customerId;

    public CreateCardParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreateCardParams cardNumber(final String cardNumber) {
        return this.with("card_number", cardNumber);
    }

    public CreateCardParams cvv2(final String cvv2) {
        return this.with("cvv2", cvv2);
    }

    public CreateCardParams expirationMonth(final Integer expirationMonth) {
        if (expirationMonth == null) {
            return this.with("expiration_month", null);
        } else {
            return this.with("expiration_month", String.format("%02d", expirationMonth));
        }
    }

    public CreateCardParams expirationYear(final Integer expirationYear) {
        if (expirationYear == null) {
            return this.with("expiration_year", null);
        } else {
            return this.with("expiration_year", String.format("%02d", expirationYear));
        }
    }

    @Deprecated
    public CreateCardParams expirationMonth(final String expirationMonth) {
        return this.with("expiration_month", expirationMonth);
    }

    @Deprecated
    public CreateCardParams expirationYear(final String expirationYear) {
        return this.with("expiration_year", expirationYear);
    }

    public CreateCardParams holderName(final String holderName) {
        return this.with("holder_name", holderName);
    }

    public CreateCardParams address(final Address address) {
        return this.with("address", address);
    }

    public CreateCardParams bankCode(final Integer bankCode) {
        if (bankCode == null) {
            return this.with("bank_code", null);
        } else {
            return this.with("bank_code", String.format("%03d", bankCode));
        }
    }

    public CreateCardParams bankCode(final String bankCode) {
        return this.with("bank_code", bankCode);
    }

}
