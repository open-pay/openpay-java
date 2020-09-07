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
 * Class: WebhookOperations.java
 *
 * Change control:
 * ---------------------------------------------------------------------------------------
 * Version | Date       | Name                                      | Description
 * ---------------------------------------------------------------------------------------
 *   1.0	2014-11-27	Marcos Coronado marcos.coronado@openpay.mx	 Creating Class.
 *
 */
package mx.openpay.client.core.operations;

import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.WEBHOOKS;
import static mx.openpay.client.utils.OpenpayPathComponents.WEBHOOKS_VERIFY;
import static mx.openpay.client.utils.OpenpayPathComponents.WEBHOOK_CODE;

import java.util.HashMap;
import java.util.List;

import mx.openpay.client.Webhook;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * <p>Clase base que contiene las operaciones disponibles para la administracion de los webhooks </p>
 *
 * @author Marcos Coronado marcos.coronado@openpay.mx
 * @since 2014-11-27
 * @version 1.0
 *
 */
public class WebhookOperations extends ServiceOperations {

	private static final String BASE_PATH = MERCHANT_ID + WEBHOOKS;

	private static final String GET_PATH = BASE_PATH + ID;

	private static final String DELETE_PATH = BASE_PATH + ID;

	private static final String VERIFY_PATH = BASE_PATH + ID + WEBHOOKS_VERIFY + WEBHOOK_CODE;

	public WebhookOperations(final JsonServiceClient client) {
        super(client);
    }

	/**
	 * <p>Método que permite crear un webhook en la plataforma Openpay</p>
	 * @param webhook Objeto contenedor de la información para la creación del webhook
	 * @return Regresa el mismo objeto Webhook, pero con el id y estado del nuevo webhook
	 */
	public Webhook create(final Webhook webhook) throws OpenpayServiceException, ServiceUnavailableException {
		String path = String.format(BASE_PATH, this.getMerchantId());
		return this.getJsonClient().post(path, webhook, Webhook.class);
	}

	/**
	 * <p>Método que permite optener la información de un webhook</p>
	 * @param webhookId  Identificador único del webhook
	 * @return Regresa un objeto Webhook
	 */
	public Webhook get(final String webhookId) throws OpenpayServiceException, ServiceUnavailableException {
		String path = String.format(GET_PATH, this.getMerchantId(), webhookId);
		return this.getJsonClient().get(path, Webhook.class);
	}

	/**
	 * <p>Método que permite eliminar un webhook en la plataforma Openpay</p>
	 * @param webhookId  Identificador único del webhook
	 */
	public void delete(final String webhookId) throws OpenpayServiceException, ServiceUnavailableException {
		String path = String.format(DELETE_PATH, this.getMerchantId(), webhookId);
		this.getJsonClient().delete(path);
	}

	/**
	 * <p>Método que permite obtener todos los webhook's de un merchant</p>
	 * @return Regresa un listado de objetos Webhook.
	 */
	public List<Webhook> list() throws OpenpayServiceException, ServiceUnavailableException {
		String path = String.format(BASE_PATH, this.getMerchantId());
		return this.getJsonClient().list(path, new HashMap<String, String>(), Webhook.class);
	}
}
