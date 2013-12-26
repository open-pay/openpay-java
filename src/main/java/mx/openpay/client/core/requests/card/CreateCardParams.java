/*
 * Copyright 2013 Opencard Inc.
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

    /**
     * ID of the customer to update. Optional, if not specified, the card will be created for the merchant.
     */
    public CreateCardParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    /**
     * Card number. Required.
     */
    public CreateCardParams cardNumber(final String cardNumber) {
        return this.with("card_number", cardNumber);
    }

    /**
     * Security code. Required only for charges.
     */
    public CreateCardParams cvv2(final String cvv2) {
        return this.with("cvv2", cvv2);
    }

    /**
     * Expiration month. Required only for charges.
     */
    public CreateCardParams expirationMonth(final Integer expirationMonth) {
        if (expirationMonth == null) {
            return this.with("expiration_month", null);
        } else {
            return this.with("expiration_month", String.format("%02d", expirationMonth));
        }
    }

    /**
     * Expiration year. Required only for charges.
     */
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

    /**
     * Card Holder name. Required.
     */
    public CreateCardParams holderName(final String holderName) {
        return this.with("holder_name", holderName);
    }

    /**
     * Card Holder address. Optional.
     */
    public CreateCardParams address(final Address address) {
        return this.with("address", address);
    }

    /**
     * Bank code. See <a
     * href="http://es.wikipedia.org/w/index.php?title=CLABE&oldid=71482742#C.C3.B3digo_de_Banco">this</a> for an
     * incomplete list as of December, 2013.
     */
    public CreateCardParams bankCode(final Integer bankCode) {
        if (bankCode == null) {
            return this.with("bank_code", null);
        } else {
            return this.with("bank_code", String.format("%03d", bankCode));
        }
    }

    /**
     * Bank code in a three-digit string.
     * @see CreateCardParams#bankCode(Integer)
     */
    public CreateCardParams bankCode(final String bankCode) {
        return this.with("bank_code", bankCode);
    }

}
