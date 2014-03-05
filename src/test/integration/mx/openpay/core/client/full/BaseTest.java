/*
 * Copyright 2014 Opencard Inc.
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
package mx.openpay.core.client.full;

import java.math.BigDecimal;
import java.util.TimeZone;

import mx.openpay.client.BankAccount;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.requests.transactions.CreateBankPayoutParams;

import org.junit.Before;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class BaseTest {

    protected OpenpayAPI api;

    @Before
    public void setupAPI() throws Exception {
        String merchantId = "miklpzr4nsvsucghm2qp";
        String apiKey = "sk_08453429e4c54220a3a82ab4d974c31a";
        String endpoint = "https://dev-api.openpay.mx/";
        this.api = new OpenpayAPI(endpoint, apiKey, merchantId);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
        // Reset Merchant balance before each test
        BigDecimal balance = this.api.merchant().get().getBalance();
        if (balance.compareTo(BigDecimal.ZERO) != 0) {
            this.api.payouts().create(
                    new CreateBankPayoutParams()
                            .amount(balance)
                            .bankAccount(new BankAccount()
                                    .clabe("012298026516924616")
                                    .holderName("Holder"))
                            .description("Payout to reset merchant's balance"));
        }
    }

}
