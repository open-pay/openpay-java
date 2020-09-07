/*
 * Copyright 2014 Opencard Inc.
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
package mx.openpay.client.core.requests.transactions;

import java.math.BigDecimal;
import java.util.Date;

import mx.openpay.client.Customer;
import mx.openpay.client.HttpContext;
import mx.openpay.client.ShipTo;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.ChargeMethods;
import mx.openpay.client.enums.OriginChannel;

/**
 * @author Luis Delucio
 */
public class CreateStoreChargeParams extends RequestBuilder {

    /**
	 *
	 */
    public CreateStoreChargeParams() {
        this.with("method", ChargeMethods.STORE.name().toLowerCase());
    }

    /**
     * The amount to charge the customer. Required. The customer should pay this exact amount.
     */
    public CreateStoreChargeParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * A description for the charge. Optional.
     */
    public CreateStoreChargeParams description(final String description) {
        return this.with("description", description);
    }

    /**
     * A custom unique ID to identify the charge. Optional.
     */
    public CreateStoreChargeParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

	/**
	 * Due date for this charge.
	 */
	public CreateStoreChargeParams dueDate(final Date dueDate) {
		return this.with("due_date", dueDate);
	}
	
	public CreateStoreChargeParams customer(final Customer customer) {
	    return this.with("customer", customer);
	}
	
	 /**
     * Sends shipping information.
     */
    public CreateCardChargeParams shipTo(final ShipTo shipTo) {
        return this.with("ship_to", shipTo);
    }

    /**
     * Sends customer browser information.
     */
    public CreateCardChargeParams httpContext(final HttpContext httpContext) {
        return this.with("http_context", httpContext);
    }

    /**
     * Sends currency information    
     */
    public CreateStoreChargeParams currency(final String currency) {
    	return this.with("currency", currency);
    }
    
    /**
     * Sends iva 
     */
    public CreateStoreChargeParams iva(final String iva) {
    	return this.with("iva", iva);
    }
    
   /**
     * Origin channel.
     *
     * @param originChannel the origin channel
     * @return the creates the store charge params
     */
    public CreateStoreChargeParams originChannel(final OriginChannel originChannel) {
       return this.with("origin_channel", originChannel.name().toLowerCase());
    }
}
