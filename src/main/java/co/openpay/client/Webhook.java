/*
 * COPYRIGHT © 2014. OPENPAY.
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
 *
 * Class: WebhookRequest.java
 * 
 * Change control:
 * ---------------------------------------------------------------------------------------
 * Version | Date       | Name                                      | Description
 * ---------------------------------------------------------------------------------------
 *   1.0	2014-11-27	Marcos Coronado marcos.coronado@openpay.mx	 Creating Class.
 *
 */
package co.openpay.client;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

import co.openpay.client.enums.WebhookStatus;

/**
 * <p>Clase utilizada como contenedor de los parametros para la creacion de un webhook </p>
 *
 * @author Marcos Coronado marcos.coronado@openpay.mx
 * @since 2014-11-27
 * @version 1.0
 *
 */
@Setter
@Getter
@ToString
public class Webhook {
	
	private String id;

	private String url; 

	private String user; 
	
	private String password;

	@SerializedName("event_types")
	private List<String> eventTypes;

	private String status;

    /**
     * Webhook id.
     * @return Returns the object Webhook
     */
	public Webhook setId(final String id) {
		this.id = id;
		return this;
	}

    /**
     * Webhook url. Required.
     * @return Returns the object Webhook
     */
	public Webhook setUrl(final String url) {
		this.url = url;
		return this;
	}

    /**
     * Webhook user.
     * @return Returns the object Webhook
     */
	public Webhook setUser(final String user) {
		this.user = user;
		return this;
	}

    /**
     * Webhook password.
     * @return Returns the object Webhook
     */
	public Webhook setPassword(final String password) {
		this.password = password;
		return this;
	}

    /**
     * Webhook eventTypes. Required.
     * @return Returns the object Webhook
     */
	public Webhook setEventTypes(final List<String> eventTypes) {
		this.eventTypes = eventTypes;
		return this;
	}
	
	/**
     * Webhook eventType. Required.
     * @return Returns the object Webhook
     */
	public Webhook addEventTypes(final String eventType) {
		if (this.eventTypes == null) {
			this.eventTypes = new ArrayList<String>();
		}
		this.eventTypes.add(eventType);
		return this;
	}

    /**
     * Webhook status.
     * @return Returns the object Webhook
     */
	public Webhook setStatus(final String status) {
		this.status = status;
		return this;
	}
	
	/**
     * Webhook status.
     * @return Returns the object WebhookStatus
     */
	public WebhookStatus getStatusEnum() {
		return WebhookStatus.valueOf(this.status.toUpperCase());
	}
}
