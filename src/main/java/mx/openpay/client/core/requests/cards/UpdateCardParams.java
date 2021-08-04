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
package mx.openpay.client.core.requests.cards;

import lombok.Getter;
import lombok.Setter;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * Update params to update card information. 
 */
@Getter
@Setter
public class UpdateCardParams extends RequestBuilder {
   
    @Getter
    private String cardId;
   
    
    public UpdateCardParams cardId(final String cardId) {
        this.cardId = cardId;
        return this;
    }

    /**
     * Security code.
     */
    public UpdateCardParams cvv2(final String cvv2) {
        return this.with("cvv2", cvv2);
    }

    /**
     * Expiration month.
     */
    public UpdateCardParams expirationMonth(final Integer expirationMonth) {
        return this.with("expiration_month", this.getTwoDigitString(expirationMonth));
    }

    /**
     * Expiration year.
     */
    public UpdateCardParams expirationYear(final Integer expirationYear) {
        return this.with("expiration_year", this.getTwoDigitString(expirationYear));
    }

    private String getTwoDigitString(final Integer number) {
        if (number == null) {
            return null;
        }
        return String.format("%02d", number);
    }

    /**
     * Expiration month, in two digits.
     */
    public UpdateCardParams expirationMonth(final String expirationMonth) {
        return this.with("expiration_month", expirationMonth);
    }

    /**
     * Expiration year, in two digits.
     */
    public UpdateCardParams expirationYear(final String expirationYear) {
        return this.with("expiration_year", expirationYear);
    }

    /**
     * Card Holder name.
     */
    public UpdateCardParams holderName(final String holderName) {
       return this.with("holder_name", holderName);
    }
}
