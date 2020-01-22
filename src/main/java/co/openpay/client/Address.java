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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an Address object.
 */
@Getter
@Setter
@ToString
public class Address {

    /** Postal code. Required. */
    @SerializedName("postal_code")
    private String postalCode;

    /** First line of address. Required. */
    private String line1;

    /** Second line of address. Optional. */
    private String line2;

    /** Third line of address. Optional. */
    private String line3;

    /** City. Required. */
    private String city;

    /** State. Required. */
    private String state;

    /** Two-letter ISO 3166-1 country code. Optional. */
    @SerializedName("country_code")
    private String countryCode;

    public Address postalCode(final String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public Address line1(final String line1) {
        this.line1 = line1;
        return this;
    }

    public Address line2(final String line2) {
        this.line2 = line2;
        return this;
    }

    public Address line3(final String line3) {
        this.line3 = line3;
        return this;
    }

    public Address city(final String city) {
        this.city = city;
        return this;
    }

    public Address state(final String state) {
        this.state = state;
        return this;
    }

    public Address countryCode(final String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

}
