package mx.openpay.client.serialization;

import mx.openpay.client.BankAccount;

import com.google.gson.JsonElement;

/**
 * Modifies the generated Card JSON to remove deserialization-only fields.
 * @author elopez
 */
public class BankAccountAdapterFactory extends OpenpayTypeAdapterFactory<BankAccount> {

    public BankAccountAdapterFactory() {
        super(BankAccount.class);
    }

    @Override
    protected void beforeWrite(final BankAccount value, final JsonElement tree) {
        if (tree.isJsonObject()) {
            tree.getAsJsonObject().remove("creation_date");
        }
    }

}
