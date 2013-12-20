/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.requests.transactions;

import java.math.BigDecimal;

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.core.requests.bank.CreateBankAccountParams;
import mx.openpay.client.enums.PayoutMethod;

/**
 * @author elopez
 */
public class CreateBankPayoutParams extends RequestBuilder {

    @Getter
    private String customerId;

    public CreateBankPayoutParams() {
        this.with("method", PayoutMethod.BANK_ACCOUNT.name().toLowerCase());
    }

    public CreateBankPayoutParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreateBankPayoutParams description(final String description) {
        return this.with("description", description);
    }

    public CreateBankPayoutParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    public CreateBankPayoutParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

    public CreateBankPayoutParams bankAccountId(final String bankAccountId) {
        return this.with("destination_id", bankAccountId);
    }

    public CreateBankPayoutParams bankAccount(final CreateBankAccountParams bankAccount) {
        if (bankAccount == null) {
            return this.with("bank_account", null);
        } else {
            return this.with("bank_account", bankAccount.asMap());
        }
    }

}
