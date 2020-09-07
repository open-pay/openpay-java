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
package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CardUpdateRequest {

    @SerializedName("holder_name")
    private String holderName;

    @SerializedName("expiration_month")
    private String expirationMonth;

    @SerializedName("expiration_year")
    private String expirationYear;

    private String cvv2;

    @SerializedName("merchant_id")
    private String merchantId;

    /**
     * Security code. Required only for charges.
     */
    public CardUpdateRequest cvv2(final String cvv2) {
        this.cvv2 = cvv2;
        return this;
    }

    /**
     * Expiration month. Required only for charges.
     */
    public CardUpdateRequest expirationMonth(final Integer expirationMonth) {
        this.expirationMonth = this.getTwoDigitString(expirationMonth);
        return this;
    }

    /**
     * Expiration year. Required only for charges.
     */
    public CardUpdateRequest expirationYear(final Integer expirationYear) {
        this.expirationYear = this.getTwoDigitString(expirationYear);
        return this;
    }

    private String getTwoDigitString(final Integer number) {
        if (number == null) {
            return null;
        }
        return String.format("%02d", number);
    }

    @Deprecated
    public CardUpdateRequest expirationMonth(final String expirationMonth) {
        this.expirationMonth = expirationMonth;
        return this;
    }

    @Deprecated
    public CardUpdateRequest expirationYear(final String expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }

    /**
     * Card Holder name. Required.
     */
    public CardUpdateRequest holderName(final String holderName) {
        this.holderName = holderName;
        return this;
    }
}
