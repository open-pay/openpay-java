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

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class Customer {

    private String name;

    private String id;

    private String email;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("phone_number")
    private String phoneNumber;

    private Address address;

    private String status;

    private BigDecimal balance;

	@SerializedName("external_id")
	private String externalId;

	@SerializedName("requires_account")
	private Boolean requiresAccount;

    @SerializedName("creation_date")
    private Date creationDate;
    
    private String clabe;
	
    /**
     * Customer first or only name. Required.
     */
    public Customer name(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Customer last name. Optional.
     */
    public Customer lastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Customer email. Required.
     */
    public Customer email(final String email) {
        this.email = email;
        return this;
    }

    /**
     * Customer phone number. Optional
     */
    public Customer phoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    /**
     * Customer address. Optional.
     */
    public Customer address(final Address address) {
        this.address = address;
        return this;
    }

	/**
	 * Customer externaId. Optional.
	 */
	public Customer externalId(final String externalId) {
		this.externalId = externalId;
		return this;
	}

	/**
	 * Customer requires account. Optional. Default true.
	 */
	public Customer requiresAccount(final Boolean requiresAccount) {
		this.requiresAccount = requiresAccount;
		return this;
	}

}
