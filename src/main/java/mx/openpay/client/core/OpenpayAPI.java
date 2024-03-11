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


import mx.openpay.client.core.operations.*;

/**
 * Initializes all Operations from the Openpay API.
 * <p>
 * A custom JsonServiceClient can be used for all the operations. If only one operation is needed in all the
 * application, it may be better to initialize a JsonServiceClient and instantiate the Operation object.
 * </p>
 * @author elopez
 */
public class OpenpayAPI {

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

    private final MerchantOperations merchantOperations;

    private final PaymentPlanOperations paymentPlanOperations;

    private final WebhookOperations webhookOperations;

    private final OpenpayFeesOperations openpayFeesOperations;

    private final TransactionsPayoutOperations transactionsPayoutOperations;

    private final OrderOperations orderOperations;

    private final BinesOperations binesOperations;

    private final CheckoutsOperations checkoutsOperations;

    private final OpenCheckoutOperations openCheckoutOperations;

    public OpenpayAPI(final String location, final String apiKey, final String merchantId) {
        this(new JsonServiceClient(location, merchantId, apiKey));
    }

    public OpenpayAPI(final String location, final String apiKey, final String merchantId, final String publicIp) {
        this(new JsonServiceClient(location, merchantId, apiKey, publicIp));
    }

    public OpenpayAPI(final JsonServiceClient client) {
        this.jsonClient = client;
        this.cardOperations = new CardOperations(this.jsonClient);
        this.bankAccountOperations = new BankAccountOperations(this.jsonClient);
        this.customerOperations = new CustomerOperations(this.jsonClient);
        this.chargeOperations = new ChargeOperations(this.jsonClient);
        this.feeOperations = new FeeOperations(this.jsonClient);
        this.payoutOperations = new PayoutOperations(this.jsonClient);
        this.transferOperations = new TransferOperations(this.jsonClient);
        this.planOperations = new PlanOperations(this.jsonClient);
        this.subscriptionsOperations = new SubscriptionOperations(this.jsonClient);
        this.merchantOperations = new MerchantOperations(this.jsonClient);
        this.openpayFeesOperations = new OpenpayFeesOperations(this.jsonClient);
        this.transactionsPayoutOperations = new TransactionsPayoutOperations(this.jsonClient);
        this.orderOperations = new OrderOperations(this.jsonClient);
        this.paymentPlanOperations = new PaymentPlanOperations(this.jsonClient);
        this.webhookOperations = new WebhookOperations(this.jsonClient);
        this.binesOperations = new BinesOperations(this.jsonClient);
        this.checkoutsOperations = new CheckoutsOperations(this.jsonClient);
        this.openCheckoutOperations =  new OpenCheckoutOperations(this.jsonClient);
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

    public MerchantOperations merchant() {
        return this.merchantOperations;
    }

    public OpenpayFeesOperations openpayFees() {
        return this.openpayFeesOperations;
    }

    public TransactionsPayoutOperations transactionsPayout() {
        return this.transactionsPayoutOperations;
    }

    @Deprecated
    public OrderOperations orders() {
        return this.orderOperations;
    }

    public PaymentPlanOperations paymentsPlans() {
        return this.paymentPlanOperations;
    }

    public WebhookOperations webhooks() {
        return this.webhookOperations;
    }

    public BinesOperations bines() {
        return this.binesOperations;
    }

    public CheckoutsOperations checkouts() {
        return this.checkoutsOperations;
    }

    public  OpenCheckoutOperations openCheckout(){return this.openCheckoutOperations;}

}
