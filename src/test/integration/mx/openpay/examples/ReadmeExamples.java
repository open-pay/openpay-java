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
package mx.openpay.examples;

import java.math.BigDecimal;

import mx.openpay.client.Address;
import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.Payout;
import mx.openpay.client.Plan;
import mx.openpay.client.Subscription;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.requests.transactions.CreateBankChargeParams;
import mx.openpay.client.core.requests.transactions.CreateBankPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.CreateCardPayoutParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.enums.PlanRepeatUnit;
import mx.openpay.client.enums.PlanStatusAfterRetry;

import org.junit.Ignore;
import org.junit.Test;

/**
 * A syntax test for the examples in the README.md file. It's not meant to run, just to check that the examples are
 * correct.
 * @author elopez
 */
@Ignore("Not actually meant to run everytime, just to check the readme examples are ok")
@SuppressWarnings("unused")
public class ReadmeExamples {

    @Test
    public void testReadmeExamples() throws Exception {
        String merchantId = "mtfsdeoulmcoj0xofpfc";
        String apiKey = "sk_4ec3ef18cd01471487ca719f566d4d3f";

        // #### Starting the API ####

        OpenpayAPI api = new OpenpayAPI("https://dev-api.openpay.mx/", apiKey, merchantId);

        Address address = new Address()
                .line1("Calle Morelos #12 - 11")
                .line2("Colonia Centro") // Optional
                .line3("Cuauhtémoc") // Optional
                .city("Distrito Federal")
                .postalCode("12345")
                .state("Queretaro")
                .countryCode("MX"); // ISO 3166-1 two-letter code

        // #### Creating a customer ####

        Customer customer = api.customers().create(new Customer()
                .name("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .phoneNumber("554-170-3567")
                .address(address));

        // #### Charging ####

        {
            Card card = new Card()
                    .cardNumber("5555555555554444") // No dashes or spaces
                    .holderName("Juan Pérez Nuñez")
                    .cvv2("422")
                    .expirationMonth(9)
                    .expirationYear(14);

            Charge charge = api.charges().create(new CreateCardChargeParams()
                    .customerId(customer.getId())
                    .description("Service charge")
                    .amount(new BigDecimal("200.00")) // Amount is in MXN
                    .orderId("Charge0001") // Optional transaction identifier
                    .card(card));

            Charge refundedCharge = api.charges().refund(new RefundParams()
                    .customerId(customer.getId())
                    .chargeId(charge.getId()));
            System.out.println("refunded: " + refundedCharge);

        }

        api.charges().create(new CreateCardChargeParams()
                .customerId(customer.getId())
                .description("Test charge")
                .amount(new BigDecimal("5000.00")) // Amount is in MXN
                .card(new Card()
                        .cardNumber("5555555555554444") // No dashes or spaces
                        .holderName("Juan Pérez Nuñez")
                        .cvv2("422")
                        .expirationMonth(9)
                        .expirationYear(14)));

        {
            Charge charge = api.charges().create(new CreateBankChargeParams()
                    .customerId(customer.getId())
                    .description("Service charge")
                    .amount(new BigDecimal("100.00"))
                    .orderId("Charge0002"));
            System.out.println("bank charge: " + charge);
            System.out.println("bank charge payment" + charge.getPaymentMethod());

        }

        // #### Payout ####

        {
            BankAccount bankAccount = new BankAccount()
                    .clabe("032180000118359719") // CLABE
                    .holderName("Juan Pérez")
                    .alias("Juan's deposit account"); // Optional

            Payout payout = api.payouts().create(new CreateBankPayoutParams()
                    .customerId(customer.getId())
                    .bankAccount(bankAccount)
                    .amount(new BigDecimal("150.00"))
                    .description("Payment to Juan")
                    .orderId("Payout00001")); // Optional transaction identifier
        }

        {
            Card card = new Card()
                    .cardNumber("5555555555554444") // No dashes or spaces
                    .holderName("Juan Pérez Nuñez")
                    .bankCode("012");

            Payout payout = api.payouts().create(new CreateCardPayoutParams()
                    .customerId(customer.getId())
                    .card(card)
                    .amount(new BigDecimal("150.00"))
                    .description("Payment to Juan")
                    .orderId("Payout00002")); // Optional transaction identifier

            System.out.println("card payout: " + payout);
        }

        // #### Subscriptions ####

        {
            Plan plan = api.plans().create(new Plan()
                    .name("Premium Subscriptions")
                    .amount(new BigDecimal("1200.00")) // Amount is in MXN
                    .repeatEvery(1, PlanRepeatUnit.MONTH)
                    .retryTimes(100)
                    .statusAfterRetry(PlanStatusAfterRetry.UNPAID));

            // After you have your plan created, you can subscribe customers to it:

            Card card = new Card()
                    .cardNumber("5555555555554444")
                    .holderName("Juan Pérez Nuñez")
                    .cvv2("422")
                    .expirationMonth(9)
                    .expirationYear(14);

            Subscription subscription = api.subscriptions().create(customer.getId(), new Subscription()
                    .planId(plan.getId())
                    .card(card)); // You can also use withCardId to use a pre-registered card.

            // To cancel the subscription at the end of the current period, you can update its cancelAtPeriodEnd
            // property to true:

            subscription.setCancelAtPeriodEnd(true);
            api.subscriptions().update(subscription);

            // You can also cancel the subscription immediately:

            api.subscriptions().delete(customer.getId(), subscription.getId());
        }
    }
}
