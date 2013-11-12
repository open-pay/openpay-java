/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
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
            tree.getAsJsonObject().remove("allows_withdrawals");
            tree.getAsJsonObject().remove("allows_deposits");
        }
    }

}
