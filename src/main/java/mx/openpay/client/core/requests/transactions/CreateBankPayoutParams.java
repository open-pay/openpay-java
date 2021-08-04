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
package mx.openpay.client.core.requests.transactions;

import java.math.BigDecimal;

import mx.openpay.client.BankAccount;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.PayoutMethod;

/**
 * Parameters to create a Bank Payout. The amount will be sent to the given Bank Account.
 * @author elopez
 */
public class CreateBankPayoutParams extends RequestBuilder {

    public CreateBankPayoutParams() {
        this.with("method", PayoutMethod.BANK_ACCOUNT.name().toLowerCase());
    }

    /**
     * A description for the payout. Optional.
     */
    public CreateBankPayoutParams description(final String description) {
        return this.with("description", description);
    }

    /**
     * The amount to send to the bank account. Required.
     */
    public CreateBankPayoutParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * A unique custom identifier for the payout. Optional.
     */
    public CreateBankPayoutParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }
    /**
     * True if you want the payout to generate settlement report.
     */
    public CreateBankPayoutParams makeBreakdown(final boolean makeBreakdown) {
        return this.with("make_breakdown", makeBreakdown);
    }

    /**
     * The ID of the Bank Account to deposit the amount to. Required if no new bank account is given.
     */
    public CreateBankPayoutParams bankAccountId(final String bankAccountId) {
        return this.with("destination_id", bankAccountId);
    }

    /**
     * A new Bank Account to deposit the amount to. Required if no existing bank account id is given.
     */
    public CreateBankPayoutParams bankAccount(final BankAccount bankAccount) {
        return this.with("bank_account", bankAccount);
    }

    public CreateBankPayoutParams currency(final String currency){
        return this.with("currency", currency);
    }
}
