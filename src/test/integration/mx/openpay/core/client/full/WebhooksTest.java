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
 * Class: WebhooksTest.java
 * 
 * Change control:
 * ---------------------------------------------------------------------------------------
 * Version | Date       | Name                                      | Description
 * ---------------------------------------------------------------------------------------
 *   1.0	2014-11-28	Marcos Coronado marcos.coronado@openpay.mx	 Creating Class.
 *
 */
package mx.openpay.core.client.full;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.Webhook;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.enums.WebhookStatus;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Clase utilizada como contenedor de los parametros para la creacion de un webhook </p>
 *
 * @author Marcos Coronado marcos.coronado@openpay.mx
 * @since 2014-11-28
 * @version 1.0
 *
 */
@Slf4j
public class WebhooksTest {

	private OpenpayAPI api;

	private String urlTest = null; 
	
	private String webhookId;
	
	private List<Webhook> webhooks;
	
	{
		try {
			urlTest = getNewURLWebhook();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void setUp() {
		String merchantId = "miklpzr4nsvsucghm2qp";
        String apiKey = "sk_08453429e4c54220a3a82ab4d974c31a";
        String endpoint = "https://dev-api.openpay.mx/";
        this.api = new OpenpayAPI(endpoint, apiKey, merchantId);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));	
	}
	
	@After
	public void tearDown() throws OpenpayServiceException, ServiceUnavailableException {
		// Elimina el webhook especificado
		if (this.webhookId != null) {
			this.api.webhooks().delete(this.webhookId);
			this.webhookId = null;
		}
		// Elimina un listado de webhook's especificados
		if (this.webhooks != null && this.webhooks.size() > 0) {
			for (Webhook webhook : this.webhooks) {
				this.api.webhooks().delete(webhook.getId());
			}
			this.webhooks = null;
		}
	}
	
	@Test
	public void testCreateWebhook() throws OpenpayServiceException, ServiceUnavailableException {
		Webhook webhook = new Webhook().setUrl(this.urlTest).addEventTypes("charge.refunded").addEventTypes("charge.failed");
		Assert.assertNull(webhook.getId());
		Assert.assertNull(webhook.getStatus());
		webhook = this.api.webhooks().create(webhook);
		log.info(webhook.toString());
		Assert.assertNotNull(webhook.getId());
		Assert.assertNotNull(webhook.getStatus());
		Assert.assertEquals(webhook.getStatusEnum(), WebhookStatus.UNVERIFIED);
		this.webhookId = webhook.getId();
	}

	@Test
	public void testVerifyWebhook() throws OpenpayServiceException, ServiceUnavailableException, ClientProtocolException, IOException {
		Webhook webhook = null;
		this.testCreateWebhook();
		webhook = this.api.webhooks().get(this.webhookId);
		Assert.assertNotNull(webhook.getId());
		Assert.assertNotNull(webhook.getStatus());
		Assert.assertEquals(webhook.getStatusEnum(), WebhookStatus.UNVERIFIED);
		log.info(webhook.toString());
		this.api.webhooks().verify(this.webhookId, this.getVerificationCodeFromURLWebhook(webhook));
		webhook = this.api.webhooks().get(this.webhookId);
		log.info(webhook.toString());
		Assert.assertNotNull(webhook.getId());
		Assert.assertNotNull(webhook.getStatus());
		Assert.assertEquals(webhook.getStatusEnum(), WebhookStatus.VERIFIED);
		this.webhookId = webhook.getId();
	}
	
	@Test
	public void testGetWebhook() throws OpenpayServiceException, ServiceUnavailableException {
		Webhook webhook = null;
		this.testCreateWebhook();
		webhook = this.api.webhooks().get(this.webhookId);
		log.info(webhook.toString());
		Assert.assertNotNull(webhook.getId());
		Assert.assertNotNull(webhook.getStatus());
		Assert.assertEquals(webhook.getStatusEnum(), WebhookStatus.UNVERIFIED);
		this.webhookId = webhook.getId();
	}
	
	@Test
	public void testDeleteWebhook() throws OpenpayServiceException, ServiceUnavailableException {
		this.testCreateWebhook();
		this.api.webhooks().delete(this.webhookId);
		this.webhookId = null;
	}
	
	@Test
	public void testListWebhook() throws OpenpayServiceException, ServiceUnavailableException, IOException {
		List<Webhook> webhooks = null;
		this.urlTest = getNewURLWebhook();
		this.testCreateWebhook();
		this.urlTest = getNewURLWebhook();
		this.testCreateWebhook();
		webhooks = this.api.webhooks().list();
		log.info(webhooks.toString());
		Assert.assertNotNull(webhooks);
		Assert.assertEquals(webhooks.size(), 2);
		this.webhookId = null;
		this.webhooks = webhooks;
	}
	
    @SuppressWarnings("deprecation")
	private String getVerificationCodeFromURLWebhook(final Webhook webhook) throws ClientProtocolException, IOException {
    	final String URL_DETAIL_WEBHOOK = "?inspect";
    	String resultString = null;
    	String verificationCode =  null;
    	
    	HttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost(webhook.getUrl() + URL_DETAIL_WEBHOOK);
    	HttpResponse response = client.execute(post);
    	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    	while (rd.read() != -1) {
    		resultString += rd.readLine();	
    	}
    	rd.close();
    	
    	verificationCode = resultString.substring(resultString.indexOf("verification_code&#34;:&#34;") + "verification_code&#34;:&#34;".length(), resultString.indexOf("verification_code&#34;:&#34;") + "verification_code&#34;:&#34;".length() + 8);
    	
    	log.info("codigo de verificacion para Webhook: " + verificationCode);
		return verificationCode;
    }
    
	private String getNewURLWebhook() throws IOException {
    	final String URL_BASE_WEBHOOK = "http://requestb.in/";
    	String resultString = null;
    	String webhookUrlId =  null;
    	
    	HttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://requestb.in/api/v1/bins");
    	HttpResponse response = client.execute(post);
    	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    	while (rd.read() != -1) {
    		resultString += rd.readLine();	
    	}
    	rd.close();
    	webhookUrlId = resultString.substring(resultString.indexOf("name") + 8, resultString.indexOf("name") + 16);
    	
    	log.info("Nueva URL de Webhook: " + URL_BASE_WEBHOOK + webhookUrlId);
		return URL_BASE_WEBHOOK + webhookUrlId;
    }
}
