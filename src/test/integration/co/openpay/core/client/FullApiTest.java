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
package co.openpay.core.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.Address;
import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.Fee;
import mx.openpay.client.Merchant;
import mx.openpay.client.Payout;
import mx.openpay.client.Plan;
import mx.openpay.client.Subscription;
import mx.openpay.client.Transfer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.requests.transactions.ConfirmCaptureParams;
import mx.openpay.client.core.requests.transactions.CreateBankChargeParams;
import mx.openpay.client.core.requests.transactions.CreateBankPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.CreateCardPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateFeeParams;
import mx.openpay.client.core.requests.transactions.CreateStoreChargeParams;
import mx.openpay.client.core.requests.transactions.CreateTransferParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.enums.PlanRepeatUnit;
import mx.openpay.client.enums.PlanStatusAfterRetry;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test creating all kinds of objects using an empty merchant account.
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Slf4j
@Ignore
public class FullApiTest {

    private OpenpayAPI api;

    private Customer customer;

    private Customer recipientCustomer;

    private Customer walletlessCustomer;

    private Customer forDeleteCustomer;

    private BankAccount merchantBankAccount;

    private BankAccount customerBankAccount;

    private BankAccount toDelMerchantBankAccount;

    private BankAccount toDelCustomerBankAccount;

    private Card customerCreditCard;

    private Card customerNoAccountCreditCard;

    private Card merchantCreditCard;

    private Card toDelMerchantCreditCard;

    private Card toDelCustomerCreditCard;

    private Card customerDebitCard;

    private Card merchantDebitCard;

    private Card customerNoAccountDebitCard;

    private Plan planWithTrial;

    private Plan planWithoutTrial;

    private Charge customerCharge;

    private Charge merchantCharge;

    private Payout merchantPayout;

    private Payout customerPayout;

    private Plan planToDelete;

    private Subscription customerSubscription;

    private Fee merchantfee;

    @Before
    public void setUp() throws Exception {
        String merchantId = "miklpzr4nsvsucghm2qp";
        String apiKey = "sk_08453429e4c54220a3a82ab4d974c31a";
        String endpoint = "https://dev-api.openpay.mx/";
        this.api = new OpenpayAPI(endpoint, apiKey, merchantId);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

    @Test
    public void testFullApi() throws Exception {
        this.testCustomers();
        this.testBankAccounts();
        this.testCards();
        this.testCardCharges();
        this.testBankCharges();
        this.testStoreCharges();
        this.testChargeGetList();
        this.testBankPayouts();
        this.testCardPayouts();
        this.testPayoutGetList();
        this.testFees();
        this.testTransfers();
        this.testPlans();
        this.testSubscriptions();
        this.testGetMerchant();
    }

    private void testCustomers() throws OpenpayServiceException, ServiceUnavailableException {
        this.testCreateCustomers();
        this.testGetAllCustomer();
        this.testGetCustomer();
        this.testUpdateCustomer();
        this.testDeleteCustomer();
    }

    // POST /v1/{merchantId}/customers
    private void testCreateCustomers() throws OpenpayServiceException, ServiceUnavailableException {
        this.customer = this.api.customers().create(
                new Customer().name("John").lastName("Doe").email("johndoe@example.com").phoneNumber("525541703567")
                        .address(getAddress()));
        log.info("Customer: {}", this.customer.getId());

        this.recipientCustomer = this.api.customers().create(
                new Customer().name("John").lastName("Doe").email("johndoe@example.com").phoneNumber("554-170-3567"));
        log.info("Recipient Customer: {}", this.recipientCustomer.getId());

        this.walletlessCustomer = this.api.customers().create(
                new Customer().name("John").lastName("Doe").email("johndoe@example.com").phoneNumber("4450090876")
                        .requiresAccount(false).address(getAddress()));
        log.info("Walletless Customer: {}", this.walletlessCustomer.getId());

        this.forDeleteCustomer = this.api.customers().create(
                new Customer().name("John").lastName("Doe").email("johndoe@example.com").phoneNumber("0090987654")
                        .requiresAccount(true).address(getAddress()).externalId("C0001"));
        log.info("Walletless Customer: {}", this.walletlessCustomer.getId());
    }

    // GET /v1/{merchantId}/customers
    private void testGetAllCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        List<Customer> customerList = this.api.customers().list(null);
        log.info("Customer list: {}", customerList);
    }

    // GET /v1/{merchantId}/customers/{customerId}
    private void testGetCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        Customer customer = this.api.customers().get(this.customer.getId());
        log.info("Customer get: {}", customer.getId());
    }

    // PUT /v1/{merchantId}/customers/{customerId}
    private void testUpdateCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        this.api.customers().update(this.customer.email("benApple@example.com").lastName("Apple").externalId("C0002"));
    }

    // DELETE /v1/{merchantId}/customers/{customerId}
    private void testDeleteCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        this.api.customers().delete(this.forDeleteCustomer.getId());
    }

    private void testBankAccounts() throws OpenpayServiceException, ServiceUnavailableException {
        this.testCreateMerchantBankAccount();
        this.testCreateCustomerBankAccount();
        this.testCreateMerchantBankAccountToDelete();
        this.testCreateCustomerBankAccountToDelete();
        this.getListMerchantBankAccounts();
        this.getListCustomerBankAccounts();
        this.getMerchantBankAccount();
        this.getCustomerBankAccount();
        this.deleteMerchantBankAccount();
        this.deleteCustomerBankAccount();
    }

    // DELETE /v1/{merchantId}/bankaccounts/{bankId}
    private void deleteMerchantBankAccount() throws ServiceUnavailableException, OpenpayServiceException {
        this.api.bankAccounts().delete(this.toDelMerchantBankAccount.getId());
    }

    // DELETE /v1/{merchantId}/customers/{customerId}/bankaccounts/{bankId}
    private void deleteCustomerBankAccount() throws ServiceUnavailableException, OpenpayServiceException {
        this.api.bankAccounts().delete(this.customer.getId(), this.toDelCustomerBankAccount.getId());
    }

    // GET /v1/{merchantId}/cbankaccounts/{bankId}
    private void getMerchantBankAccount() throws ServiceUnavailableException, OpenpayServiceException {
        BankAccount banckAccount = this.api.bankAccounts().get(this.merchantBankAccount.getId());
        log.info("Merchant BankAccount get: {}", banckAccount);
    }

    // GET /v1/{merchantId}/customers/{customerId}/bankaccounts/{bankId}
    private void getCustomerBankAccount() throws ServiceUnavailableException, OpenpayServiceException {
        BankAccount banckAccount = this.api.bankAccounts().get(this.customer.getId(), this.customerBankAccount.getId());
        log.info("Customer BankAccount get: {}", banckAccount);
    }

    // GET /v1/{merchantId}/bankaccounts
    private void getListMerchantBankAccounts() throws ServiceUnavailableException, OpenpayServiceException {
        List<BankAccount> listBanckAccounts = this.api.bankAccounts().list(null);
        log.info("Merchant BankAccount list: {}", listBanckAccounts);
    }

    // GET /v1/{merchantId}/customers/{customerId}/bankaccounts
    private void getListCustomerBankAccounts() throws ServiceUnavailableException, OpenpayServiceException {
        List<BankAccount> listBanckAccounts = this.api.bankAccounts().list(this.customer.getId(), null);
        log.info("Customer BankAccount list: {}", listBanckAccounts);
    }

    // POST /v1/{merchantId}/bankaccounts
    private void testCreateMerchantBankAccount() throws OpenpayServiceException, ServiceUnavailableException {
        this.merchantBankAccount = this.api.bankAccounts().create(new BankAccount().clabe("032180000118359719") // CLABE
                .holderName("Juan Pérez"));
        log.info("Merchant bank account: {}", this.merchantBankAccount.getId());

    }

    // POST /v1/{merchantId}/customers/{customerId}/bankaccounts
    private void testCreateCustomerBankAccount() throws OpenpayServiceException, ServiceUnavailableException {
        this.customerBankAccount = this.api.bankAccounts().create(
                this.customer.getId(),
                new BankAccount().clabe("032180000118359719") // CLABE
                        .holderName("Juan Pérez"));
        log.info("Customer bank account: {}", this.customerBankAccount.getId());
    }

    // POST /v1/{merchantId}/bankaccounts
    private void testCreateMerchantBankAccountToDelete() throws OpenpayServiceException, ServiceUnavailableException {
        this.toDelMerchantBankAccount = this.api.bankAccounts().create(new BankAccount().clabe("044180001456789038") // CLABE
                .holderName("Juan Pérez"));
        log.info("Merchant bank account: {}", this.toDelMerchantBankAccount.getId());

    }

    // POST /v1/{merchantId}/customers/{customerId}/bankaccounts
    private void testCreateCustomerBankAccountToDelete() throws OpenpayServiceException, ServiceUnavailableException {
        this.toDelCustomerBankAccount = this.api.bankAccounts().create(
                this.customer.getId(),
                new BankAccount().clabe("044180001456789038") // CLABE
                        .holderName("Juan Pérez"));
        log.info("Customer bank account: {}", this.toDelCustomerBankAccount.getId());
    }

    private void testCards() throws OpenpayServiceException, ServiceUnavailableException {
        this.testCreateMerchantCard();
        this.testCreateCustomerCard();
        this.testCreateCustomerNoAccountCard();
        this.testCreateMerchantCardToDelete();
        this.testCreateMerchantDebitCard();
        this.testCreateCustomerDebitCard();
        this.testCreateCustomerNoAccountDebitCard();
        this.testCreateCustomerCardToDelete();
        this.testListMerchantCards();
        this.testListCustomerCards();
        this.testGetMerchantCard();
        this.testGetCustomerCard();
        this.testDeleteMerchantCard();
        this.testDeleteCustomerCard();
    }

    private void testDeleteCustomerCard() throws ServiceUnavailableException, OpenpayServiceException {
        this.api.cards().delete(this.customer.getId(), this.toDelCustomerCreditCard.getId());
    }

    private void testDeleteMerchantCard() throws ServiceUnavailableException, OpenpayServiceException {
        this.api.cards().delete(this.toDelMerchantCreditCard.getId());
    }

    // GET /v1/{merchantId}/customers/{customerId}/cards/{cardId}
    private void testGetCustomerCard() throws ServiceUnavailableException, OpenpayServiceException {
        Card card = this.api.cards().get(this.customer.getId(), this.customerDebitCard.getId());
        log.info("Customer get card", card);
    }

    // GET /v1/{merchantId}/cards/{cardId}
    private void testGetMerchantCard() throws ServiceUnavailableException, OpenpayServiceException {
        Card card = this.api.cards().get(this.merchantCreditCard.getId());
        log.info("Merchant get card", card);
    }

    // GET /v1/{merchantId}/cards
    private void testListMerchantCards() throws ServiceUnavailableException, OpenpayServiceException {
        List<Card> listCards = this.api.cards().list(null);
        log.info("Merchant Card list: {}", listCards);

    }

    // GET /v1/{merchantId}/customers/{customerId}/cards
    private void testListCustomerCards() throws ServiceUnavailableException, OpenpayServiceException {
        List<Card> listCards = this.api.cards().list(this.customer.getId(), null);
        log.info("Customer Card list: {}", listCards);

    }

    // POST /v1/{merchantId}/cards
    private void testCreateMerchantCard() throws OpenpayServiceException, ServiceUnavailableException {
        this.merchantCreditCard = this.api.cards().create(this.createCard("5105105105105100"));
        log.info("Merchant credit card: {}", this.merchantCreditCard.getId());

    }

    // POST /v1/{merchantId}/cards
    private void testCreateMerchantCardToDelete() throws OpenpayServiceException, ServiceUnavailableException {
        this.toDelMerchantCreditCard = this.api.cards().create(this.createCard("345678000000007"));
        log.info("Merchant credit card: {}", this.toDelMerchantCreditCard.getId());

    }

    // POST /v1/{merchantId}/cards
    private void testCreateCustomerCardToDelete() throws OpenpayServiceException, ServiceUnavailableException {
        this.toDelCustomerCreditCard = this.api.cards().create(
                this.customer.getId(),
                this.createCard("345678000000007"));
        log.info("Customer credit card: {}", this.toDelCustomerCreditCard.getId());

    }

    // POST /v1/{merchantId}/customers/{customerId}/cards
    private void testCreateCustomerCard() throws OpenpayServiceException, ServiceUnavailableException {
        this.customerCreditCard = this.api.cards().create(this.customer.getId(), this.createCard("5555555555554444"));
        log.info("Customer credit card: {}", this.customerCreditCard.getId());
    }

    private void testCreateCustomerNoAccountCard() throws OpenpayServiceException, ServiceUnavailableException {
        this.customerNoAccountCreditCard = this.api.cards().create(this.walletlessCustomer.getId(),
                this.createCard("5555555555554444"));
        log.info("Customer Without account credit card: {}", this.customerNoAccountCreditCard.getId());
    }

    // POST /v1/{merchantId}/cards
    private void testCreateMerchantDebitCard() throws OpenpayServiceException, ServiceUnavailableException {
        this.merchantDebitCard = this.api.cards().create(this.createCard("4111111111111111"));
        log.info("Merchant debit card: {}", this.merchantDebitCard.getId());

    }

    // POST /v1/{merchantId}/customers/{customerId}/cards
    private void testCreateCustomerDebitCard() throws OpenpayServiceException, ServiceUnavailableException {
        this.customerDebitCard = this.api.cards().create(this.customer.getId(), this.createCard("4111111111111111"));
        log.info("Customer debit card: {}", this.customerDebitCard.getId());
    }

    private void testCreateCustomerNoAccountDebitCard() throws OpenpayServiceException, ServiceUnavailableException {
        this.customerNoAccountDebitCard = this.api.cards().create(this.walletlessCustomer.getId(),
                this.createCard("4111111111111111"));
        log.info("Customer without Account debit card: {}", this.customerNoAccountDebitCard.getId());
    }

    private void testCardCharges() throws OpenpayServiceException, ServiceUnavailableException {
        this.testChargeMerchantCard();
        this.testChargeCustomerCard();
        this.testChargeCustomerNoAccountCard();
        this.testChargeMerchantCardCapture();
        this.testChargeCustomerDirectCardCapture();
        this.testChargeCustomerWalletlessDirectCard();
        this.testChargeMerchantDirectCard();
        this.testChargeCustomerDirectCard();
        this.testChargeMerchantCardCaptureRefund();
        this.testChargeCustomerDirectCardCaptureRefund();

        this.testChargeMerchantDirectCard_NotEnoughFunds();
        this.testChargeCustomerDirectCard_NotEnoughFunds();
    }

    // POST /v1/{merchantId}/charges/{transactionId}/refund
    private void testChargeMerchantCardCaptureRefund() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(
                new CreateCardChargeParams().description("Service charge").amount(new BigDecimal("200.00"))
                        // Amount is in MXN
                        .orderId(String.valueOf(System.currentTimeMillis())).cardId(this.merchantCreditCard.getId())
                        .capture(Boolean.FALSE));
        log.info("Merchant card charge: {}", charge.getId());

        charge = this.api.charges().confirmCapture(
                new ConfirmCaptureParams().amount(new BigDecimal("200.00")).chargeId(charge.getId()));
        log.info("Merchant card charge confirmed: {}", charge.getId());

        charge = this.api.charges().refund(new RefundParams().chargeId(charge.getId()));
        log.info("Merchant card charge refunded: {}", charge.getRefund().getId());

    }

    // POST
    // /v1/{merchantId}/customers/{customerId}/charges/{transactionId}/refund
    private void testChargeCustomerDirectCardCaptureRefund() throws OpenpayServiceException,
            ServiceUnavailableException {
        Charge charge = this.api.charges().create(
                this.customer.getId(),
                new CreateCardChargeParams().description("Service charge").amount(new BigDecimal("200.00"))
                        .card(this.createCard("5555555555554444")).capture(Boolean.FALSE));
        log.info("Customer direct card charge: {}", charge.getId());

        charge = this.api.charges().confirmCapture(
                this.customer.getId(),
                new ConfirmCaptureParams().amount(new BigDecimal("200.00")).chargeId(charge.getId()));
        log.info("Customer direct card charge confirmed: {}", charge.getId());

        charge = this.api.charges().refund(this.customer.getId(), new RefundParams().chargeId(charge.getId()));
        log.info("Customer card charge refunded: {}", charge.getRefund().getId());
    }

    private void testChargeMerchantDirectCard_NotEnoughFunds() throws OpenpayServiceException,
            ServiceUnavailableException {
        try {
            this.api.charges().create(new CreateCardChargeParams()
                    .description("Not enough funds merchant")
                    .amount(new BigDecimal("20.15")) // Amount is in MXN
                    .card(this.createCard("4444444444444448")));
            fail("Expected not enough funds merchant");
        } catch (OpenpayServiceException e) {
            log.info("Merchant charge without funds: {}", e.getBody());
        }
    }

    private void testChargeCustomerDirectCard_NotEnoughFunds() throws OpenpayServiceException,
            ServiceUnavailableException {
        try {
            this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                    .description("Not enough funds customer")
                    .amount(new BigDecimal("20.15")) // Amount is in MXN
                    .card(this.createCard("4444444444444448")));
            fail("Expected not enough funds customer");
        } catch (OpenpayServiceException e) {
            log.info("Customer charge without funds: {}", e.getBody());
        }

    }

    // POST
    // /v1/{merchantId}/customers/{customerId}/charges/{transactionId}/capture
    private void testChargeCustomerDirectCardCapture() throws OpenpayServiceException, ServiceUnavailableException {
        this.customerCharge = this.api.charges().create(
                this.customer.getId(),
                new CreateCardChargeParams().description("Service charge").amount(new BigDecimal("200.00"))
                        .card(this.createCard("5555555555554444")).capture(Boolean.FALSE));
        log.info("Customer direct card charge: {}", this.customerCharge.getId());

        this.customerCharge = this.api.charges().confirmCapture(
                this.customer.getId(),
                new ConfirmCaptureParams().amount(new BigDecimal("200.00")).chargeId(this.customerCharge.getId()));
        log.info("Customer direct card charge confirmed: {}", this.customerCharge.getId());
    }

    // POST /v1/{merchantId}/charges/{transactionId}/capture
    private void testChargeMerchantCardCapture() throws OpenpayServiceException, ServiceUnavailableException {
        this.merchantCharge = this.api.charges().create(
                new CreateCardChargeParams().description("Service charge").amount(new BigDecimal("200.00"))
                        // Amount is in MXN
                        .orderId(String.valueOf(System.currentTimeMillis())).cardId(this.merchantCreditCard.getId())
                        .capture(Boolean.FALSE));
        log.info("Merchant card charge: {}", this.merchantCharge.getId());

        this.merchantCharge = this.api.charges().confirmCapture(
                new ConfirmCaptureParams().amount(new BigDecimal("200.00")).chargeId(this.merchantCharge.getId()));
        log.info("Merchant card charge confirmed: {}", this.merchantCharge.getId());

    }

    // POST /v1/{merchantId}/charges
    private void testChargeMerchantCard() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(
                new CreateCardChargeParams().description("Service charge").amount(new BigDecimal("200.00")) // Amount
                                                                                                            // is
                                                                                                            // in
                                                                                                            // MXN
                        .orderId(String.valueOf(System.currentTimeMillis())).cardId(this.merchantCreditCard.getId()));
        log.info("Merchant card charge: {}", charge.getId());
    }

    // POST /v1/{merchantId}/customers/{customerId}/charges
    private void testChargeCustomerCard() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(
                this.customer.getId(),
                new CreateCardChargeParams().description("Service charge").amount(new BigDecimal("200.00")) // Amount
                                                                                                            // is
                                                                                                            // in
                                                                                                            // MXN
                        .cardId(this.customerCreditCard.getId()));
        log.info("Customer card charge: {}", charge.getId());
    }

    private void testChargeCustomerNoAccountCard() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(this.walletlessCustomer.getId(), new CreateCardChargeParams()
                .description("Service charge")
                .amount(new BigDecimal(".15")) // Amount is in MXN
                .cardId(this.customerNoAccountCreditCard.getId()));
        log.info("Customer card charge: {}", charge.getId());
    }

    // POST /v1/{merchantId}/customers/{customerId}/charges
    private void testChargeCustomerWalletlessDirectCard() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(
                this.walletlessCustomer.getId(),
                new CreateCardChargeParams().description("Service charge").amount(new BigDecimal("200.00"))
                        .card(this.createCard("5555555555554444")));
        log.info("Customer direct card charge: {}", charge.getId());
    }

    // POST /v1/{merchantId}/charges
    private void testChargeMerchantDirectCard() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(
                new CreateCardChargeParams().description("Service charge")
                        .amount(new BigDecimal("200.00")).card(this.createCard("345678000000007")));
        log.info("Merchant direct card charge: {}", charge.getId());

    }

    // POST /v1/{merchantId}/customers/{customerId}/charges
    private void testChargeCustomerDirectCard() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(
                this.customer.getId(),
                new CreateCardChargeParams().description("Service charge").amount(new BigDecimal("200.00"))
                        .card(this.createCard("5555555555554444")));
        log.info("Customer direct card charge: {}", charge.getId());
    }

    private void testBankCharges() throws OpenpayServiceException, ServiceUnavailableException {
        this.testChargeMerchantBank();
        this.testChargeCustomerBank();
    }

    // POST /v1/{merchantId}/charges
    private void testChargeMerchantBank() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(
                new CreateBankChargeParams().amount(new BigDecimal("100")).description("Cargo por banco"));
        log.info("Merchant bank Charge: {}", charge.getId());
    }

    // POST /v1/{merchantId}/customers/{customerId}/charges
    private void testChargeCustomerBank() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(
                this.customer.getId(),
                new CreateBankChargeParams().amount(new BigDecimal("100")).description("Cargo por banco"));
        log.info("Customer bank Charge: {}", charge.getId());
    }

    private void testStoreCharges() throws OpenpayServiceException, ServiceUnavailableException {
        this.testChargeMerchantStore();
        this.testChargeCustomerStore();
    }

    // POST /v1/{merchantId}/charges
    private void testChargeMerchantStore() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(
                new CreateStoreChargeParams().amount(new BigDecimal("100")).description("Cargo por tienda"));
        log.info("Merchant store Charge: {}", charge.getId());
    }

    // POST /v1/{merchantId}/customers/{customerId}/charges
    private void testChargeCustomerStore() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(
                this.customer.getId(),
                new CreateStoreChargeParams().amount(new BigDecimal("100")).description("Cargo por tienda"));
        log.info("Customer store Charge: {}", charge.getId());
    }

    private void testChargeGetList() throws OpenpayServiceException, ServiceUnavailableException {
        this.testMerchantListCharges();
        this.testCustomerListCharges();
        this.testMerchantGetCharge();
        this.testCustomerGetCharge();
    }

    // GET /v1/{merchantId}/customers/{customerId}/charges/{transactionId}
    private void testCustomerGetCharge() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().get(this.customer.getId(), this.customerCharge.getId());
        log.info("Merchant charge {} ", charge);
    }

    // GET /v1/{merchantId}/charges/{transactionId}
    private void testMerchantGetCharge() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().get(this.merchantCharge.getId());
        log.info("Merchant charge {} ", charge);
    }

    // GET /v1/{merchantId}/customers/{customerId}/charges
    private void testCustomerListCharges() throws OpenpayServiceException, ServiceUnavailableException {
        List<Charge> chargesList = this.api.charges().list(this.customer.getId(), null);
        log.info("Customer charges list {}", chargesList);
    }

    // GET /v1/{merchantId}/charges
    private void testMerchantListCharges() throws OpenpayServiceException, ServiceUnavailableException {
        List<Charge> chargesList = this.api.charges().list(null);
        log.info("Merchant charges list {}", chargesList);

    }

    private void testBankPayouts() throws OpenpayServiceException, ServiceUnavailableException {
        // this.testBankPayoutMerchant();
        this.testBankPayoutCustomer();
        // this.testBankPayoutMerchantDirect();
        this.testBankPayoutCustomerDirect();

        this.testCancelBankPayoutCustomer();
        this.testCancelBankPayoutCustomerDirect();
    }

    // POST /v1/{merchantId}/payouts
    private void testBankPayoutMerchant() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(
                new CreateBankPayoutParams().amount(new BigDecimal("20")).description("Customer payout")
                        .bankAccountId(this.merchantBankAccount.getId()));
        log.info("Merchant bank payout: {}", payout.getId());
    }

    // POST /v1/{merchantId}/customers/{customerId}/payouts
    private void testBankPayoutCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(
                this.customer.getId(),
                new CreateBankPayoutParams().amount(new BigDecimal("20")).description("Customer payout")
                        .bankAccountId(this.customerBankAccount.getId()));
        log.info("Customer bank payout: {}", payout.getId());

    }

    // POST /v1/{merchantId}/payouts
    private void testBankPayoutMerchantDirect() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(
                new CreateBankPayoutParams().amount(new BigDecimal("20")).description("Merchant payout")
                        .bankAccount(new BankAccount().clabe("032180000118359719") // CLABE
                                .holderName("Juan Pérez")));
        log.info("Merchant direct bank payout: {}", payout.getId());
    }

    // POST /v1/{merchantId}/customers/{customerId}/payouts
    private void testBankPayoutCustomerDirect() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(
                this.customer.getId(),
                new CreateBankPayoutParams().amount(new BigDecimal("20")).description("Customer payout")
                        .bankAccount(new BankAccount().clabe("032180000118359719") // CLABE
                                .holderName("Juan Pérez")));
        log.info("Customer direct bank payout: {}", payout.getId());
    }

    private void testCardPayouts() throws OpenpayServiceException, ServiceUnavailableException {
        // this.testCardPayoutMerchant();
        this.testCardPayoutCustomer();
        // this.testCardPayoutMerchantDirect();
        this.testCardPayoutCustomerDirect();
    }

    private void testCancelBankPayoutCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(this.customer.getId(), new CreateBankPayoutParams()
                .amount(new BigDecimal("20"))
                .description("Customer payout")
                .bankAccountId(this.customerBankAccount.getId()));
        log.info("Customer bank payout to cancel: {}", payout.getId());
        this.api.payouts().cancel(this.customer.getId(), payout.getId());

    }

    private void testCancelBankPayoutCustomerDirect() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(this.customer.getId(), new CreateBankPayoutParams()
                .amount(new BigDecimal("20"))
                .description("Customer payout")
                .bankAccount(new BankAccount()
                        .clabe("032180000118359719") // CLABE
                        .holderName("Juan Pérez")));
        log.info("Customer direct bank payout to cancel: {}", payout.getId());
        this.api.payouts().cancel(this.customer.getId(), payout.getId());
    }

    // POST /v1/{merchantId}/payouts
    private void testCardPayoutMerchant() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(
                new CreateCardPayoutParams().amount(new BigDecimal("20")).description("Merchant payout")
                        .cardId(this.merchantDebitCard.getId()));
        log.info("Merchant card payout: {}", payout.getId());
    }

    // POST /v1/{merchantId}/customers/{customerId}/payouts
    private void testCardPayoutCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(
                this.customer.getId(),
                new CreateCardPayoutParams().amount(new BigDecimal("20")).description("Customer payout")
                        .cardId(this.customerDebitCard.getId()));
        log.info("Customer card payout: {}", payout.getId());
    }

    // POST /v1/{merchantId}/payouts
    private void testCardPayoutMerchantDirect() throws OpenpayServiceException, ServiceUnavailableException {
        this.merchantPayout = this.api.payouts().create(
                new CreateCardPayoutParams().amount(new BigDecimal("20")).description("Merchant payout")
                        .card(this.createCard("4111111111111111").cvv2(null)));
        log.info("Merchant direct card payout: {}", this.merchantPayout.getId());
    }

    // POST /v1/{merchantId}/customers/{customerId}/payouts
    private void testCardPayoutCustomerDirect() throws OpenpayServiceException, ServiceUnavailableException {
        this.customerPayout = this.api.payouts().create(
                this.customer.getId(),
                new CreateCardPayoutParams().amount(new BigDecimal("20")).description("Customer payout")
                        .card(this.createCard("4111111111111111").cvv2(null)));
        log.info("Customer direct card payout: {}", this.customerPayout.getId());
    }

    private void testPayoutGetList() throws OpenpayServiceException, ServiceUnavailableException {
        this.testPayoutMerchantList();
        this.testPayoutCustomerList();
//        this.testPayoutMerchantGet();
        this.testPayoutCustomerGet();
    }

    // GET /v1/{merchantId}/payouts/{transactionId}
    private void testPayoutMerchantGet() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().get(this.merchantPayout.getId());
        log.info("Merchant payout get {}", payout);

    }

    // GET /v1/{merchantId}/customers/{customerId}/payouts/{transactionId}
    private void testPayoutCustomerGet() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().get(this.customer.getId(), this.customerPayout.getId());
        log.info("Customer payout get {}", payout);
    }

    // GET /v1/{merchantId}/customers/{customerId}/payouts
    private void testPayoutCustomerList() throws OpenpayServiceException, ServiceUnavailableException {
        List<Payout> payoutsList = this.api.payouts().list(this.customer.getId(), null);
        log.info("Customer payouts list {}", payoutsList);
    }

    // GET /v1/{merchantId}/payouts
    private void testPayoutMerchantList() throws OpenpayServiceException, ServiceUnavailableException {
        List<Payout> payoutList = this.api.payouts().list(null);
        log.info("Merchant Payouts list {}", payoutList);

    }

    // GET /v1/{merchantId}/fees/{transactionId}
    private void testFees() throws OpenpayServiceException, ServiceUnavailableException {
        this.testCreatetFee();
        this.testGetFees();
        this.testListFees();
    }

    // GET /v1/{merchantId}/fees/{transactionId}
    private void testGetFees() throws OpenpayServiceException, ServiceUnavailableException {
        Fee fee = this.api.fees().get(this.merchantfee.getId());
        log.info("Merchant fee get {}", fee);

    }

    // GET /v1/{merchantId}/fees
    private void testListFees() throws OpenpayServiceException, ServiceUnavailableException {
        List<Fee> feeList = this.api.fees().list(null);
        log.info("Merchant fee list {}", feeList);
    }

    // POST /v1/{merchantId}/fees
    private void testCreatetFee() throws OpenpayServiceException, ServiceUnavailableException {
        this.merchantfee = this.api.fees().create(
                new CreateFeeParams().amount(new BigDecimal("5")).customerId(this.customer.getId())
                        .description("Use fee"));
        log.info("Fee: {}", this.merchantfee.getId());

    }

    private void testTransfers() throws OpenpayServiceException, ServiceUnavailableException {
        this.testCreateTransfer();
        this.testGetTransfer();
        this.testListTransfers();
    }

    // GET /v1/{merchantId}/customers/{customerId}/transfers
    private void testListTransfers() throws OpenpayServiceException, ServiceUnavailableException {
        List<Transfer> transferList = this.api.transfers().list(this.recipientCustomer.getId(), null);
        log.info("Transfer list {}", transferList);

    }

    // GET /v1/{merchantId}/customers/{customerId}/transfers/{transactionId}
    private void testGetTransfer() throws OpenpayServiceException, ServiceUnavailableException {
        Transfer transfer = this.api.transfers().create(
                this.customer.getId(),
                new CreateTransferParams().toCustomerId(this.recipientCustomer.getId()).amount(new BigDecimal("100"))
                        .description("Transfer to another customer"));
        log.info("Transfer: {}", transfer.getId());

        Transfer transfer2 = this.api.transfers().get(this.recipientCustomer.getId(), transfer.getId());
        log.info("Transfer get {}", transfer2);

    }

    // POST /v1/{merchantId}/customers/{customerId}/transfers
    private void testCreateTransfer() throws OpenpayServiceException, ServiceUnavailableException {
        Transfer transfer = this.api.transfers().create(
                this.customer.getId(),
                new CreateTransferParams().toCustomerId(this.recipientCustomer.getId()).amount(new BigDecimal("100"))
                        .description("Transfer to another customer"));
        log.info("Transfer: {}", transfer.getId());
    }

    private void testPlans() throws OpenpayServiceException, ServiceUnavailableException {
        this.testCreatePlanNoTrial();
        this.testCreatePlanTrial();
        this.testCreatePlanToDelete();
        this.testListPlans();
        this.testGetPlan();
        this.testUpdatePlan();
        this.testDeletePlan();
    }

    // DELETE /v1/{merchantId}/plans/{planId}
    private void testDeletePlan() throws OpenpayServiceException, ServiceUnavailableException {
        this.api.plans().delete(this.planToDelete.getId());
    }

    // PUT /v1/{merchantId}/plans/{planId}
    private void testUpdatePlan() throws OpenpayServiceException, ServiceUnavailableException {
        Plan plan = this.api.plans().update(this.planToDelete.name("This is the plan to delete").trialDays(1));
        log.info("Plan updated {}", plan);
    }

    // GET /v1/{merchantId}/plans/{planId}
    private void testGetPlan() throws OpenpayServiceException, ServiceUnavailableException {
        Plan plan = this.api.plans().get(this.planToDelete.getId());
        log.info("Plan get {}", plan);

    }

    // GET /v1/{merchantId}/plans
    private void testListPlans() throws OpenpayServiceException, ServiceUnavailableException {
        List<Plan> planList = this.api.plans().list(null);
        log.info("Plan List {}", planList);
    }

    // POST /v1/{merchantId}/plans
    private void testCreatePlanToDelete() throws OpenpayServiceException, ServiceUnavailableException {
        this.planToDelete = this.api.plans().create(
                new Plan().name("Plan to delete").amount(new BigDecimal("100")).repeatEvery(1, PlanRepeatUnit.WEEK)
                        .statusAfterRetry(PlanStatusAfterRetry.CANCELLED));
        log.info("Plan without trial: {}", this.planToDelete.getId());

    }

    // POST /v1/{merchantId}/plans
    private void testCreatePlanNoTrial() throws OpenpayServiceException, ServiceUnavailableException {
        this.planWithoutTrial = this.api.plans().create(
                new Plan().name("Plan without trial").amount(new BigDecimal("100")).repeatEvery(1, PlanRepeatUnit.WEEK)
                        .statusAfterRetry(PlanStatusAfterRetry.CANCELLED));
        log.info("Plan without trial: {}", this.planWithoutTrial.getId());
    }

    // POST /v1/{merchantId}/plans
    private void testCreatePlanTrial() throws OpenpayServiceException, ServiceUnavailableException {
        this.planWithTrial = this.api.plans().create(
                new Plan().name("Plan with trial").amount(new BigDecimal("100")).trialDays(30)
                        .repeatEvery(3, PlanRepeatUnit.WEEK).statusAfterRetry(PlanStatusAfterRetry.UNPAID));
        log.info("Plan with trial: {}", this.planWithTrial.getId());
    }

    private void testSubscriptions() throws OpenpayServiceException, ServiceUnavailableException {
        this.testSubscribeCustomerCardExists();
        this.testSubscribeCustomerNewCard();
        this.testSubscribeCustomerAddTrialDate();
        this.testSubscribeCustomerRemoveTrial();
        this.testUpdateCustomerSubscription();
        this.testListCustomerSubscriptions();
        this.testGetCustomerSubscription();
        this.testGetSubscriptionByPlan();
        this.testListSubscriptionByPlan();
        this.testDeleteCustomerSubscription();
    }

    // GET /v1/{merchantId}/plans/{planId}/subscriptions
    private void testListSubscriptionByPlan() throws OpenpayServiceException, ServiceUnavailableException {
        List<Subscription> subscriptionList = this.api.plans().listSubscriptions(this.planWithTrial.getId(), null);
        log.info("Suscription list by plan {}", subscriptionList);

    }

    // GET /v1/{merchantId}/plans/{planId}/subscriptions/{subscriptionId}
    private void testGetSubscriptionByPlan() throws OpenpayServiceException, ServiceUnavailableException {
        Subscription subscription = this.api.subscriptions().getByPlan(
                this.planWithoutTrial.getId(),
                this.customerSubscription.getId());
        log.info("subscription by plan {}", subscription);
    }

    // GET
    // /v1/{merchantId}/customers/{customerId}/subscriptions/{subscriptionId}
    private void testGetCustomerSubscription() throws OpenpayServiceException, ServiceUnavailableException {
        Subscription subscription = this.api.subscriptions().get(
                this.customer.getId(),
                this.customerSubscription.getId());
        log.info("Customer subscription get {}", subscription);

    }

    // GET /v1/{merchantId}/customers/{customerId}/subscriptions
    private void testListCustomerSubscriptions() throws OpenpayServiceException, ServiceUnavailableException {
        List<Subscription> subscriptionList = this.api.subscriptions().list(this.customer.getId(), null);
        log.info("Customer Subscription List {}", subscriptionList);
    }

    // DELETE
    // /v1/{merchantId}/customers/{customerId}/subscriptions/{subscriptionId}
    private void testDeleteCustomerSubscription() throws OpenpayServiceException, ServiceUnavailableException {
        this.api.subscriptions().delete(this.customer.getId(), this.customerSubscription.getId());

    }

    // PUT
    // /v1/{merchantId}/customers/{customerId}/subscriptions/{subscriptionId}
    private void testUpdateCustomerSubscription() throws OpenpayServiceException, ServiceUnavailableException {
        Subscription subscription = this.api.subscriptions().update(
                this.customerSubscription.card(this.customerDebitCard));
        log.info("Customer subscription {}", subscription);
    }

    // POST /v1/{merchantId}/customers/{customerId}/subscriptions
    private void testSubscribeCustomerCardExists() throws OpenpayServiceException, ServiceUnavailableException {
        this.customerSubscription = this.api.subscriptions().create(
                this.customer.getId(),
                new Subscription().planId(this.planWithoutTrial.getId()).cardId(this.customerCreditCard.getId()));
        log.info("Subscription existing card: {}", this.customerSubscription.getId());
    }

    // POST /v1/{merchantId}/customers/{customerId}/subscriptions
    private void testSubscribeCustomerNewCard() throws OpenpayServiceException, ServiceUnavailableException {
        Subscription subscription = this.api.subscriptions().create(
                this.customer.getId(),
                new Subscription().planId(this.planWithTrial.getId()).card(this.createCard("5555555555554444")));
        log.info("Subscription new card: {}", subscription.getId());
    }

    // POST /v1/{merchantId}/customers/{customerId}/subscriptions
    private void testSubscribeCustomerAddTrialDate() throws OpenpayServiceException, ServiceUnavailableException {
        Date oneDayAfter = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
        Subscription subscription = this.api.subscriptions().create(
                this.customer.getId(),
                new Subscription().cardId(this.customerCreditCard.getId()).planId(this.planWithoutTrial.getId())
                        .trialEndDate(oneDayAfter));
        log.info("Subscription adding trial: {}", subscription.getId());
        assertThat(subscription.getStatus(), is("trial"));
    }

    // POST /v1/{merchantId}/customers/{customerId}/subscriptions
    private void testSubscribeCustomerRemoveTrial() throws OpenpayServiceException, ServiceUnavailableException {
        Date oneDayBefore = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
        Subscription subscription = this.api.subscriptions().create(
                this.customer.getId(),
                new Subscription().cardId(this.customerCreditCard.getId()).planId(this.planWithTrial.getId())
                        .trialEndDate(oneDayBefore));
        log.info("Subscription removing trial: {}", subscription.getId());
        assertThat(subscription.getStatus(), is("active"));
    }

    // GET /v1/{merchantId}/merchant
    private void testGetMerchant() throws OpenpayServiceException, ServiceUnavailableException {
        Merchant merchant = this.api.merchant().get();
        log.info("Merchant name {}, balance {}", merchant.getName(), merchant.getBalance());
    }

    private Card createCard(final String number) {
        return new Card().cardNumber(number) // No dashes or spaces
                .holderName("Juan Pérez Nuñez")
                .cvv2(number.startsWith("3") ? "1234" : "422")
                .expirationMonth(5).expirationYear(17);
    }

    private static final Address getAddress() {
        return new Address().line1("Calle Morelos #12 - 11").city("Distrito Federal").postalCode("12345")
                .state("Queretaro").countryCode("MX");
    }

}
