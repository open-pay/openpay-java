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

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
public class Card {

    @SerializedName("creation_date")
    private Date creationDate;

    private String id;

    @SerializedName("bank_name")
    private String bankName;

    @SerializedName("allows_payouts")
    private Boolean allowsPayouts;

    @SerializedName("holder_name")
    private String holderName;

    @SerializedName("expiration_month")
    private String expirationMonth;

    @SerializedName("expiration_year")
    private String expirationYear;

    private Address address;

    @SerializedName("card_number")
    private String cardNumber;

    private String brand;

    @SerializedName("allows_charges")
    private Boolean allowsCharges;

    @SerializedName("bank_code")
    private String bankCode;

    @SerializedName("token_id")
    private String tokenId;

    private String type;

    private String cvv2;

    /**
     * Card number. Required.
     */
    public Card cardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    /**
     * Security code. Required only for charges.
     */
    public Card cvv2(final String cvv2) {
        this.cvv2 = cvv2;
        return this;
    }

    /**
     * Expiration month. Required only for charges.
     */
    public Card expirationMonth(final Integer expirationMonth) {
        this.expirationMonth = this.getTwoDigitString(expirationMonth);
        return this;
    }

    /**
     * Expiration year. Required only for charges.
     */
    public Card expirationYear(final Integer expirationYear) {
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
    public Card expirationMonth(final String expirationMonth) {
        this.expirationMonth = expirationMonth;
        return this;
    }

    @Deprecated
    public Card expirationYear(final String expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }

    /**
     * Card Holder name. Required.
     */
    public Card holderName(final String holderName) {
        this.holderName = holderName;
        return this;
    }

    /**
     * Card Holder address. Optional.
     */
    public Card address(final Address address) {
        this.address = address;
        return this;
    }

    /**
     * Bank code. See <a
     * href="http://es.wikipedia.org/w/index.php?title=CLABE&oldid=71482742#C.C3.B3digo_de_Banco">this</a> for an
     * incomplete list as of December, 2013.
     */
    public Card bankCode(final Integer bankCode) {
        if (bankCode == null) {
            this.bankCode = null;
        } else {
            this.bankCode = String.format("%03d", bankCode);
        }
        return this;
    }

    /**
     * Bank code in a three-digit string.
     * @see CreateCardParams#bankCode(Integer)
     */
    public Card bankCode(final String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    /**
     * Token previously generated in Openpay.
     */
    public Card tokenId(final String tokenId) {
        this.tokenId = tokenId;
        return this;
    }

}
