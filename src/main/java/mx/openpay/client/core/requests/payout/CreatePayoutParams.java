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
public class CreatePayoutParams extends RequestBuilder {

    @Getter
    private String customerId;

    public CreatePayoutParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreatePayoutParams cardId(final String cardId) {
        this.with("method", PayoutMethod.CARD.name().toLowerCase());
        return this.with("destination_id", cardId);
    }

    public CreatePayoutParams card(final Card card) {
        this.with("method", PayoutMethod.CARD.name().toLowerCase());
        return this.with("card", card);
    }

    public CreatePayoutParams description(final String description) {
        return this.with("description", description);
    }

    public CreatePayoutParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    public CreatePayoutParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

    public CreatePayoutParams bankAccountId(final String bankAccountId) {
        this.with("method", PayoutMethod.BANK_ACCOUNT.name().toLowerCase());
        return this.with("destination_id", bankAccountId);
    }

    public CreatePayoutParams bankAccount(final BankAccount bankAccount) {
        this.with("method", PayoutMethod.BANK_ACCOUNT.name().toLowerCase());
        return this.with("bank_account", bankAccount);
    }

}
