/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.requests.payout;

import java.math.BigDecimal;

import lombok.Getter;
import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.PayoutMethod;

/**
 * @author elopez
 */
public class CreatePayout extends RequestBuilder {

    @Getter
    private String customerId;

    public CreatePayout withCustomerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreatePayout withCardId(final String cardId) {
        this.with("method", PayoutMethod.CARD.name().toLowerCase());
        return this.with("destination_id", cardId);
    }

    public CreatePayout withCard(final Card card) {
        this.with("method", PayoutMethod.CARD.name().toLowerCase());
        return this.with("card", card);
    }

    public CreatePayout withDescription(final String description) {
        return this.with("description", description);
    }

    public CreatePayout withAmount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    public CreatePayout withOrderId(final String orderId) {
        return this.with("order_id", orderId);
    }

    public CreatePayout withBankAccountId(final String bankAccountId) {
        this.with("method", PayoutMethod.BANK_ACCOUNT.name().toLowerCase());
        return this.with("destination_id", bankAccountId);
    }

    public CreatePayout withBankAccount(final BankAccount bankAccount) {
        this.with("method", PayoutMethod.BANK_ACCOUNT.name().toLowerCase());
        return this.with("bank_account", bankAccount);
    }

}
