package mx.openpay.client.core.groups;

import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.operations.groups.GroupCardOperations;
import mx.openpay.client.core.operations.groups.GroupChargeOperations;
import mx.openpay.client.core.operations.groups.GroupCustomerOperations;

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

    public OpenpayGroupAPI(final String location, final String apiKey, final String merchantId) {
        this(new JsonServiceClient(location, merchantId, apiKey));
    }

    public OpenpayGroupAPI(final JsonServiceClient client) {
        this.jsonClient = client;
        this.groupCustomerOperations = new GroupCustomerOperations(this.jsonClient);
        this.groupCardOperations = new GroupCardOperations(this.jsonClient);
        this.groupChargeOperations = new GroupChargeOperations(this.jsonClient);
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
}
