/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
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
        this.jsonClient.setConnectionTimeout(timeout);
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
}
