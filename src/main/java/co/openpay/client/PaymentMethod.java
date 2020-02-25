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
package co.openpay.client;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

/**
 * @author elopez
 */
@Getter
@Setter
@ToString
public class PaymentMethod {

    private String type;

    private String bank;
    
    private String clabe;

    private String name;
    
    private String agreement;

    private String reference;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("ivr_key")
    private String ivrKey;

    @SerializedName("paybin_reference")
    private String paybinReference;

    @SerializedName("barcode_url")
    private String barcodeUrl;

    @SerializedName("barcode_paybin_url")
    private String barcodePaybinUrl;

    @SerializedName("payment_address")
    private String paymentAddress;

    @SerializedName("payment_url_bip21")
    private String paymentUrlBip21;

    @SerializedName("amount_bitcoins")
    private BigDecimal amountBitcoins;

    @SerializedName("exchange_rate")
    private ExchangeRate exchangeRate;

    private String url;
}
