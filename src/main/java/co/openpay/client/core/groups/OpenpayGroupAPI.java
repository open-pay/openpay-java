package co.openpay.client.core.groups;

import co.openpay.client.core.JsonServiceClient;
import co.openpay.client.core.operations.groups.GroupCardOperations;
import co.openpay.client.core.operations.groups.GroupChargeOperations;
import co.openpay.client.core.operations.groups.GroupCustomerOperations;
import co.openpay.client.core.operations.groups.GroupSubscriptionOperations;

/**
 * API for group operations. A group allows operations with customers shared between the merchants that belong to the
 * same group.
 * @author elopez
 */
public class OpenpayGroupAPI {

    private final JsonServiceClient jsonClient;

    private final GroupCustomerOperations groupCustomerOperations;

    private final GroupCardOperations groupCardOperations;

    private final GroupChargeOperations groupChargeOperations;

    private final GroupSubscriptionOperations groupSubscriptionOperations;

    public OpenpayGroupAPI(final String location, final String apiKey, final String merchantId) {
        this(new JsonServiceClient(location, merchantId, apiKey));
    }

    public OpenpayGroupAPI(final JsonServiceClient client) {
        this.jsonClient = client;
        this.groupCustomerOperations = new GroupCustomerOperations(this.jsonClient);
        this.groupCardOperations = new GroupCardOperations(this.jsonClient);
        this.groupChargeOperations = new GroupChargeOperations(this.jsonClient);
        this.groupSubscriptionOperations = new GroupSubscriptionOperations(this.jsonClient);
    }

    public GroupCustomerOperations groupCustomers() {
        return this.groupCustomerOperations;
    }

    public GroupCardOperations groupCards() {
        return this.groupCardOperations;
    }

    public GroupChargeOperations groupCharges() {
        return this.groupChargeOperations;
    }

    public GroupSubscriptionOperations groupSubscriptions(){
        return this.groupSubscriptionOperations;
    }
}
