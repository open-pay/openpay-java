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

}
