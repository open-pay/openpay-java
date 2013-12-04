package mx.openpay.client.serialization;

import mx.openpay.client.Card;

import com.google.gson.JsonElement;

/**
 * Modifies the generated Card JSON to remove deserialization-only fields.
 * @author elopez
 */
public class CardAdapterFactory extends OpenpayTypeAdapterFactory<Card> {

    public CardAdapterFactory() {
        super(Card.class);
    }

    @Override
    protected void beforeWrite(final Card value, final JsonElement tree) {
        if (tree.isJsonObject()) {
            tree.getAsJsonObject().remove("allows_charges");
            tree.getAsJsonObject().remove("allows_payouts");
            tree.getAsJsonObject().remove("creation_date");
        }
    }

}
