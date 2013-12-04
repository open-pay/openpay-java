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
