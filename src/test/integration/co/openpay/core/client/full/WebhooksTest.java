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
package co.openpay.core.client.full;

import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import co.openpay.client.Webhook;
import co.openpay.client.core.OpenpayAPI;
import co.openpay.client.enums.WebhookStatus;
import co.openpay.client.exceptions.OpenpayServiceException;
import co.openpay.client.exceptions.ServiceUnavailableException;

/**
 * <p>
 * Clase utilizada como contenedor de los parametros para la creacion de un webhook
 * </p>
 * @author Marcos Coronado marcos.coronado@openpay.mx
 * @since 2014-11-28
 * @version 1.0
 */
@Slf4j
@Ignore
public class WebhooksTest {

    private OpenpayAPI api;

    private String urlTest = null;

    private String webhookId;

    private List<Webhook> webhooks;

    {
        try {
            this.urlTest = this.getNewURLWebhook();
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
        Webhook webhook = new Webhook().setUrl(this.urlTest).addEventTypes("charge.refunded")
                .addEventTypes("charge.failed");
        Assert.assertNull(webhook.getId());
        Assert.assertNull(webhook.getStatus());
        webhook = this.api.webhooks().create(webhook);
        log.info(webhook.toString());
        Assert.assertNotNull(webhook.getId());
        Assert.assertNotNull(webhook.getStatus());
        Assert.assertEquals(WebhookStatus.VERIFIED, webhook.getStatusEnum());
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
        Assert.assertEquals(WebhookStatus.VERIFIED, webhook.getStatusEnum());
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
        this.urlTest = this.getNewURLWebhook();
        this.testCreateWebhook();
        this.urlTest = this.getNewURLWebhook();
        this.testCreateWebhook();
        webhooks = this.api.webhooks().list();
        this.webhooks = webhooks;
        this.webhookId = null;
        log.info(webhooks.toString());
        Assert.assertNotNull(webhooks);
        Assert.assertEquals(2, webhooks.size());
    }

    private String getNewURLWebhook() throws IOException {
        return "http://httpbin.org/post?t=" + System.currentTimeMillis();
    }
}
