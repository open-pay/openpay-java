package mx.openpay.client.serialization;

import mx.openpay.client.Customer;

import com.google.gson.JsonElement;

/**
 * Modifies the generated Customer JSON to remove deserialization-only fields.
 * @author elopez
 */
public class CustomerAdapterFactory extends OpenpayTypeAdapterFactory<Customer> {

    public CustomerAdapterFactory() {
        super(Customer.class);
    }

    @Override
    protected void beforeWrite(final Customer value, final JsonElement tree) {
        if (tree.isJsonObject()) {
            tree.getAsJsonObject().remove("status");
            tree.getAsJsonObject().remove("creation_date");
            tree.getAsJsonObject().remove("balance");
        }
    }

}
