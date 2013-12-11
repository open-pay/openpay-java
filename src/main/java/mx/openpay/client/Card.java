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
    private boolean allowsPayouts;

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
    private boolean allowsCharges;

    @SerializedName("bank_code")
    private String bankCode;

    private String type;

    private String cvv2;

    public void setExpirationMonth(int expirationMonth) {
        this.expirationMonth = String.valueOf(expirationMonth);
    }

    public void setExpirationYear(int expirationYear) {
        this.expirationMonth = String.valueOf(expirationYear);
    }

}
