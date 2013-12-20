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
package mx.openpay.client.core;

import static mx.openpay.client.utils.OpenpayPathComponents.VERSION;

import java.security.GeneralSecurityException;

import lombok.Getter;
import mx.openpay.client.core.operations.BankAccountOperations;
import mx.openpay.client.core.operations.CardOperations;
import mx.openpay.client.core.operations.ChargeOperations;
import mx.openpay.client.core.operations.CustomerOperations;
import mx.openpay.client.core.operations.FeeOperations;
import mx.openpay.client.core.operations.PayoutOperations;
import mx.openpay.client.core.operations.PlanOperations;
import mx.openpay.client.core.operations.SubscriptionOperations;
import mx.openpay.client.core.operations.TransferOperations;

/**
 * @author elopez
 */
public class OpenpayAPI {

    private static final String HTTP_RESOURCE_SEPARATOR = "/";

    @Getter
    private final String location;

    @Getter
    private final String merchantId;

    @Getter
    private final String apiKey;

    private final JsonServiceClient jsonClient;

    private final BankAccountOperations bankAccountOperations;

    private final CustomerOperations customerOperations;

    private final CardOperations cardOperations;

    private final ChargeOperations chargeOperations;

    private final FeeOperations feeOperations;

    private final PayoutOperations payoutOperations;

    private final TransferOperations transferOperations;

    private final PlanOperations planOperations;

    private final SubscriptionOperations subscriptionsOperations;

    public OpenpayAPI(final String location, final String apiKey, final String merchantId) {
        if (location == null) {
            throw new IllegalArgumentException("Location can't be null");
        }
        if (merchantId == null) {
            throw new IllegalArgumentException("Merchant ID can't be null");
        }
        this.location = location;
        this.merchantId = merchantId;
        this.apiKey = apiKey;
        this.jsonClient = this.initJsonClient(location, apiKey);
        this.bankAccountOperations = new BankAccountOperations(this.jsonClient, merchantId);
        this.customerOperations = new CustomerOperations(this.jsonClient, merchantId);
        this.cardOperations = new CardOperations(this.jsonClient, merchantId);
        this.chargeOperations = new ChargeOperations(this.jsonClient, merchantId);
        this.feeOperations = new FeeOperations(this.jsonClient, merchantId);
        this.payoutOperations = new PayoutOperations(this.jsonClient, merchantId);
        this.transferOperations = new TransferOperations(this.jsonClient, merchantId);
        this.planOperations = new PlanOperations(this.jsonClient, merchantId);
        this.subscriptionsOperations = new SubscriptionOperations(this.jsonClient, merchantId);
    }

    private JsonServiceClient initJsonClient(final String location, final String apiKey) {
        StringBuilder baseUri = new StringBuilder();
        if (location.contains("http") || location.contains("https")) {
            baseUri.append(location.replace("http:", "https:"));
        } else {
            baseUri.append("https://").append(location);
        }
        if (!location.endsWith(HTTP_RESOURCE_SEPARATOR)) {
            baseUri.append(HTTP_RESOURCE_SEPARATOR);
        }
        baseUri.append(VERSION);
        try {
            return new JsonServiceClient(baseUri.toString(), apiKey);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Can't initialize Openpay Client", e);
        }
    }

    public void setTimeout(final int timeout) {
        this.jsonClient.getHttpClient().setConnectionTimeout(timeout);
    }

    public CustomerOperations customers() {
        return this.customerOperations;
    }

    public CardOperations cards() {
        return this.cardOperations;
    }

    public ChargeOperations charges() {
        return this.chargeOperations;
    }

    public FeeOperations fees() {
        return this.feeOperations;
    }

    public PayoutOperations payouts() {
        return this.payoutOperations;
    }

    public TransferOperations transfers() {
        return this.transferOperations;
    }

    public BankAccountOperations bankAccounts() {
        return this.bankAccountOperations;
    }

    public PlanOperations plans() {
        return this.planOperations;
    }

    public SubscriptionOperations subscriptions() {
        return this.subscriptionsOperations;
    }
}
