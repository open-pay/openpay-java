/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
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
        String merchantId = "mi93pk0cjumoraf08tqt";
        String apiKey = "sk_88ab47ebc710472d91488cc4f3009080";
        String endpoint = "https://sandbox-api.openpay.mx/";
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
