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
package co.openpay.client.core.requests.transactions;

import java.math.BigDecimal;

import co.openpay.client.Card;
import co.openpay.client.core.requests.RequestBuilder;
import co.openpay.client.enums.PayoutMethod;

/**
 * Parameters to create a payout to a card.
 * @author elopez
 */
public class CreateCardPayoutParams extends RequestBuilder {

    public CreateCardPayoutParams() {
        this.with("method", PayoutMethod.CARD.name().toLowerCase());
    }

    /**
     * The ID of the card to send the funds to. Required if no new card is given. The card must allow payouts.
     */
    public CreateCardPayoutParams cardId(final String cardId) {
        return this.with("destination_id", cardId);
    }

    /**
     * A new card to send the funds to. Required if no card id is given. The card must have a bank code to allow
     * payouts.
     */
    public CreateCardPayoutParams card(final Card card) {
        return this.with("card", card);
    }

    /**
     * A description for the payout. Optional.
     */
    public CreateCardPayoutParams description(final String description) {
        return this.with("description", description);
    }

    /**
     * The amount to send to the card, in MXN. Required.
     */
    public CreateCardPayoutParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * A custom unique identifier for the Payout. Optional.
     */
    public CreateCardPayoutParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

}
